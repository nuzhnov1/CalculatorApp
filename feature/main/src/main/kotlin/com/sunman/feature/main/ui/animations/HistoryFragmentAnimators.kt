package com.sunman.feature.main.ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import com.sunman.feature.main.ui.resources.animDuration


internal sealed interface HistoryFragmentAnimatorWrapper

internal class HistoryFragmentHideAnimatorWrapper(
    view: View,
    startAlphaValue: Float
) : HistoryFragmentAnimatorWrapper {

    val animator: ObjectAnimator = ObjectAnimator.ofFloat(
        /* target = */ view,
        /* propertyName = */ "alpha",
        /* ...values = */ startAlphaValue, 0f
    ).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.disable()
                view.update()
            }

            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.INVISIBLE
                view.update()
            }

            override fun onAnimationCancel(animation: Animator) {
                view.alpha = 0f
                view.visibility = View.INVISIBLE
                view.update()
            }
        })

        duration = view.context.resources.animDuration
    }
}

internal class HistoryFragmentShowAnimatorWrapper(
    view: View,
    startAlphaValue: Float
) : HistoryFragmentAnimatorWrapper {

    val animator: ObjectAnimator = ObjectAnimator.ofFloat(
        /* target = */ view,
        /* propertyName = */ "alpha",
        /* ...values = */ startAlphaValue, 1f
    ).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.visibility = View.VISIBLE
                view.update()
            }

            override fun onAnimationEnd(animation: Animator) {
                view.enable()
                view.update()
            }

            override fun onAnimationCancel(animation: Animator) {
                view.alpha = 1f
                view.enable()
                view.update()
            }
        })

        duration = view.context.resources.animDuration
    }
}
