package com.sunman.feature.main.ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.sunman.feature.main.ui.resources.historyFragmentMinHeight


internal object HistoryFragmentOnLayoutChangeListener : View.OnLayoutChangeListener {
    var isAnimating = false

    override fun onLayoutChange(
        view: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        leftOld: Int,
        topOld: Int,
        rightOld: Int,
        bottomOld: Int
    ) {
        if (view == null || isAnimating) {
            return
        }

        val minHeight = view.context.resources.historyFragmentMinHeight

        if (view.visibility == View.VISIBLE && view.measuredHeight < minHeight) {
            isAnimating = true

            view.hideAnimator.apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) { isAnimating = false }
                })
                start()
            }
        } else if (view.visibility == View.INVISIBLE && view.measuredHeight >= minHeight) {
            isAnimating = true

            view.showAnimator.apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) { isAnimating = false }
                })
                start()
            }
        }
    }
}


internal fun View.changeVisibilityImmediately() {
    val minHeight = context.resources.historyFragmentMinHeight

    visibility = if (measuredHeight < minHeight) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}
