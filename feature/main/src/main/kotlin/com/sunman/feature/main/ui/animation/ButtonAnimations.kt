package com.sunman.feature.main.ui.animation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.sunman.feature.main.R

@SuppressLint("ClickableViewAccessibility")
fun ViewGroup.setAnimationsOnAllButtons(): Unit = children.forEach {
    when (it) {
        is ViewGroup -> it.setAnimationsOnAllButtons()

        is Button, is ImageButton -> it.setOnTouchListener { view, motionEvent ->
            view.animateOnClick(motionEvent)
            true
        }

        else -> Unit
    }
}

private fun View.animateOnClick(motionEvent: MotionEvent) = when (motionEvent.actionMasked) {
    MotionEvent.ACTION_DOWN -> {
        buttonCornerResizeAnimator.start()
        buttonChangingAlphaAnimator?.start()
    }

    MotionEvent.ACTION_UP -> {
        performClick()
        if (id == R.id.collapseButton) {
            (this as ImageButton).animateArrowRotation()
        }

        buttonCornerResizeAnimator.reverse()
        buttonChangingAlphaAnimator?.reverse()
    }

    MotionEvent.ACTION_CANCEL -> setToDefaultState()

    else -> Unit
}

private fun ImageButton.animateArrowRotation() {
    val currentAnimation = drawable as AnimatedVectorDrawableCompat

    val arrowDown = ResourcesCompat.getDrawable(
        context.resources,
        R.drawable.ic_baseline_keyboard_arrow_down_24,
        null
    ) as AnimatedVectorDrawableCompat

    val arrowUp = ResourcesCompat.getDrawable(
        context.resources,
        R.drawable.ic_baseline_keyboard_arrow_up_24,
        null
    ) as AnimatedVectorDrawableCompat

    val nextAnimation = if (currentAnimation.constantState == arrowDown.constantState) {
        arrowUp
    } else {
        arrowDown
    }

    currentAnimation.apply {
        registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                setImageDrawable(nextAnimation)
            }
        })

        start()
    }
}

private val View.buttonCornerResizeAnimator: ValueAnimator get() {
    val buttonCornerRadius = context.resources.getDimension(R.dimen.cornerRadius)
    val pressedButtonCornerRadius = buttonCornerRadius / 4

    return ValueAnimator.ofFloat(buttonCornerRadius, pressedButtonCornerRadius).apply {
        addUpdateListener {
            val shape = background as GradientDrawable

            shape.cornerRadius = it.animatedValue as Float
            invalidate()
        }

        duration = context.resources.getInteger(R.integer.animationDuration).toLong()
    }
}

private val View.buttonChangingAlphaAnimator: ObjectAnimator? get() {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        val endAlpha = context.resources.getDimension(R.dimen.pressedButtonAlpha)

        ObjectAnimator.ofFloat(this, "alpha", alpha, endAlpha).apply {
            duration = context.resources.getInteger(R.integer.animationDuration).toLong()
        }
    } else {
        null
    }
}

private fun View.setToDefaultState() {
    val shape = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        background as GradientDrawable
    } else {
        (background as RippleDrawable).getDrawable(0) as GradientDrawable
    }

    shape.cornerRadius = context.resources.getDimension(R.dimen.cornerRadius)
    alpha = 1f
}
