package com.sunman.feature.main.ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import com.sunman.feature.main.databinding.MainSubpanelBinding


internal fun MainSubpanelBinding.animateFunctionsSubpanelCollapse() =
    functionsSubpanel?.collapseAnimator?.run {
        addFunctionsSubpanelAnimatorListener(animator = this)
        start()
    }

internal fun MainSubpanelBinding.animateFunctionsSubpanelExpand() =
    functionsSubpanel?.expandAnimator?.run {
        addFunctionsSubpanelAnimatorListener(animator = this)
        start()
    }

private fun MainSubpanelBinding.addFunctionsSubpanelAnimatorListener(animator: Animator) {
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            collapseButton?.isEnabled = false
        }

        override fun onAnimationEnd(animation: Animator) {
            collapseButton?.isEnabled = true
        }

        override fun onAnimationCancel(animation: Animator) {
            collapseButton?.isEnabled = true
        }
    })
}
