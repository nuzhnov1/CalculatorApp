package com.sunman.feature.calculationpanel.ui.animations

import android.animation.*
import android.view.View
import android.widget.TableLayout
import com.sunman.feature.calculationpanel.R

fun View.animateCollapse() {
    ValueAnimator.ofInt(measuredHeight, 0).apply {
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
    }.start()
}

fun View.animateExpand() {
    measure(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)

    ValueAnimator.ofInt(0, measuredHeight).apply {
        visibility = View.VISIBLE
        layoutParams.height = 1

        addUpdateListener {
            layoutParams.height = it.animatedValue as Int
            requestLayout()
            invalidate()
        }

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                layoutParams.height = TableLayout.LayoutParams.WRAP_CONTENT
                isFocusable = true
            }
        })

        duration = context.resources.getInteger(R.integer.animationDuration).toLong()
    }.start()
}

fun View.animateReplaceView(newView: View, replaceView: () -> Unit) {
    val animationDuration = context.resources.getInteger(R.integer.animationDuration).toLong()

    val hideOldViewAnimator = ObjectAnimator
        .ofFloat(this, "alpha", alpha, 0f)
        .apply {
            duration = animationDuration / 2
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    isFocusable = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    replaceView()
                }
            })
        }
    val showNewViewAnimator = ObjectAnimator
        .ofFloat(newView, "alpha", 0f, 1f)
        .apply {
            duration = animationDuration / 2
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    isFocusable = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    isFocusable = true
                }
            })
        }

    AnimatorSet().apply {
        play(showNewViewAnimator).after(hideOldViewAnimator)
    }.start()
}
