package com.sunman.feature.main.ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import com.sunman.feature.main.ui.resources.animDuration


internal val View.hideAnimator get() = ObjectAnimator.ofFloat(
    /* target = */ this,
    /* propertyName = */ "alpha",
    /* ...values = */ 0f
).apply {
    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) { disable() }
        override fun onAnimationEnd(animation: Animator) { visibility = View.INVISIBLE }

        override fun onAnimationCancel(animation: Animator) {
            alpha = 0f
            visibility = View.INVISIBLE
        }
    })
    duration = context.resources.animDuration
}

internal val View.showAnimator get() = ObjectAnimator.ofFloat(
    /* target = */ this,
    /* propertyName = */ "alpha",
    /* ...values = */ 1f
).apply {
    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) { visibility = View.VISIBLE }
        override fun onAnimationEnd(animation: Animator) { enable() }

        override fun onAnimationCancel(animation: Animator) {
            alpha = 1f
            enable()
        }
    })
    duration = context.resources.animDuration
}
