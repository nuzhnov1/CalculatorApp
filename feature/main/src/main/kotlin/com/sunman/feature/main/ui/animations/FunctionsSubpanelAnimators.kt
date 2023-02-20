package com.sunman.feature.main.ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.sunman.feature.main.databinding.FunctionsSubpanelBinding
import com.sunman.feature.main.ui.resources.animDuration


internal val FunctionsSubpanelBinding.collapseAnimator get() = root.run {
    ValueAnimator.ofInt(height, 0).apply {
        addUpdateListener {
            layoutParams.height = it.animatedValue as Int
            update()
        }

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                disable()
                update()
            }

            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
                update()
            }

            override fun onAnimationCancel(animation: Animator) {
                layoutParams.height = 0
                visibility = View.GONE
                update()
            }
        })

        duration = context.resources.animDuration
    }
}

internal val FunctionsSubpanelBinding.expandAnimator: ValueAnimator get() = root.run {
    measure(MATCH_PARENT, WRAP_CONTENT)

    return ValueAnimator.ofInt(0, root.measuredHeight).apply {
        addUpdateListener {
            layoutParams.height = it.animatedValue as Int
            update()
        }

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                visibility = View.VISIBLE
                disable()
                update()
            }

            override fun onAnimationEnd(animation: Animator) {
                enable()
                update()
            }

            override fun onAnimationCancel(animation: Animator) {
                layoutParams.height = measuredHeight
                enable()
                update()
            }
        })

        duration = context.resources.animDuration
    }
}
