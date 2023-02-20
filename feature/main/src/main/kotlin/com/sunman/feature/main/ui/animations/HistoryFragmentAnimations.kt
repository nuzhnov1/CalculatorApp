package com.sunman.feature.main.ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.sunman.feature.main.ui.resources.historyFragmentMinHeight


internal fun View.changeVisibilityImmediately() {
    val minHeight = context.resources.historyFragmentMinHeight

    visibility = if (measuredHeight < minHeight) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}


internal object HistoryFragmentOnLayoutChangeListener : View.OnLayoutChangeListener {

    private var animatorWrapper: HistoryFragmentAnimatorWrapper? = null


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
        if (view == null) {
            return
        }

        val minHeight = view.context.resources.historyFragmentMinHeight

        if (view.visibility == View.VISIBLE && view.measuredHeight < minHeight &&
            animatorWrapper !is HistoryFragmentHideAnimatorWrapper) {

            animatorWrapper = HistoryFragmentHideAnimatorWrapper(view, view.alpha).apply {
                animator.apply {
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) { animatorWrapper = null }
                    })

                    start()
                }
            }
        } else if (view.visibility == View.INVISIBLE && view.measuredHeight >= minHeight &&
            animatorWrapper !is HistoryFragmentShowAnimatorWrapper) {

            animatorWrapper = HistoryFragmentShowAnimatorWrapper(view, view.alpha).apply {
                animator.apply {
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) { animatorWrapper = null }
                    })

                    start()
                }
            }
        }
    }
}
