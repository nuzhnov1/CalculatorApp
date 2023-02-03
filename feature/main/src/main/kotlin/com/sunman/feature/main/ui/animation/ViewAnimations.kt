package com.sunman.feature.main.ui.animation

import android.animation.*
import android.view.View
import android.view.ViewGroup
import com.sunman.feature.main.R

fun View.getAnimatorOfVerticalCollapse(): ValueAnimator = ValueAnimator.ofInt(height, 0).apply {
    addUpdateListener {
        layoutParams.height = it.animatedValue as Int
        requestLayout()
        invalidate()
    }

    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            isFocusable = false
        }

        override fun onAnimationEnd(animation: Animator) {
            visibility = View.GONE
        }
    })

    duration = context.resources.getInteger(R.integer.animationDuration).toLong()
}

fun View.getAnimatorOfVerticalExpand(layoutParameters: ViewGroup.LayoutParams): ValueAnimator {
    measure(layoutParameters.width, layoutParameters.height)

    return ValueAnimator.ofInt(0, measuredHeight).apply {
        visibility = View.VISIBLE
        layoutParams.height = 1

        addUpdateListener {
            layoutParams.height = it.animatedValue as Int
            requestLayout()
            invalidate()
        }

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                requestLayout()
                invalidate()

                isFocusable = true
            }
        })

        duration = context.resources.getInteger(R.integer.animationDuration).toLong()
    }
}

fun View.getAnimatorOfReplaceView(newView: View, replaceView: () -> Unit): AnimatorSet {
    val animationDuration = context.resources.getInteger(R.integer.animationDuration).toLong()

    val hideOldViewAnimator = ObjectAnimator
        .ofFloat(this, "alpha", alpha, 0f)
        .apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    isFocusable = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    replaceView()
                }
            })

            duration = animationDuration / 2
        }

    val showNewViewAnimator = ObjectAnimator
        .ofFloat(newView, "alpha", 0f, 1f)
        .apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    isFocusable = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    isFocusable = true
                }
            })

            duration = animationDuration / 2
        }

    return AnimatorSet().apply { play(showNewViewAnimator).after(hideOldViewAnimator) }
}
