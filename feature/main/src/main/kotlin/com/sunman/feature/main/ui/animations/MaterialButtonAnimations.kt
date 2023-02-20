package com.sunman.feature.main.ui.animations

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.MotionEvent
import androidx.core.view.allViews
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.button.MaterialButton
import com.sunman.feature.main.ui.*
import com.sunman.feature.main.ui.resources.*


@SuppressLint("ClickableViewAccessibility")
internal fun MainFragment.setAnimationsOnAllMaterialButtons() {
    binding.root.allViews.forEach {
        if (it is MaterialButton) {
            it.setOnTouchListener { _, motionEvent -> it.animateOnClick(motionEvent) }
        }
    }

    binding.mainSubpanel.collapseButton?.setCollapseButtonAnimation()
}

@SuppressLint("ClickableViewAccessibility")
private fun MaterialButton.setCollapseButtonAnimation() {
    var direction = ArrowDirection.DOWN

    icon = context.getArrowAVD(direction)
    setOnTouchListener { _, motionEvent ->
        if (motionEvent.actionMasked == MotionEvent.ACTION_UP) {
            direction = direction.changeDirection()

            val currentAVD = icon as AnimatedVectorDrawableCompat
            val nextAVD = context.getArrowAVD(direction)

            currentAVD.apply {
                registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) { icon = nextAVD }
                })

                start()
            }
        }

        animateOnClick(motionEvent)
    }
}

private fun MaterialButton.animateOnClick(motionEvent: MotionEvent) =
    when (motionEvent.actionMasked) {
        MotionEvent.ACTION_HOVER_ENTER -> {
            hoveredAnimatorSet.start()
            false
        }

        MotionEvent.ACTION_HOVER_EXIT -> {
            defaultAnimatorSet.start()
            true
        }

        MotionEvent.ACTION_DOWN -> {
            pressedAnimatorSet.start()
            false
        }

        MotionEvent.ACTION_UP -> {
            defaultAnimatorSet.start()
            false
        }

        MotionEvent.ACTION_CANCEL -> {
            setToDefaultStateImmediately()
            true
        }

        else -> false
    }

private fun MaterialButton.setToDefaultStateImmediately() = context.resources.run {
    alpha = btnAlpha
    cornerRadius = btnCornerRadius

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        translationZ = btnTranslationZ
    }
}
