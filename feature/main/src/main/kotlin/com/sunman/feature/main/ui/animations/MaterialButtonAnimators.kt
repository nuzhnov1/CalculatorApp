package com.sunman.feature.main.ui.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import com.google.android.material.button.MaterialButton
import com.sunman.feature.main.ui.resources.*


private val MaterialButton.hoveredAlphaAnimator get() = ObjectAnimator.ofFloat(
    /* target = */ this,
    /* propertyName = */ "alpha",
    /* ...values = */ context.resources.btnHoveredAlpha
)

private val MaterialButton.pressedAlphaAnimator get() = ObjectAnimator.ofFloat(
    /* target = */ this,
    /* propertyName = */ "alpha",
    /* ...values = */ context.resources.btnPressedAlpha
)

private val MaterialButton.defaultAlphaAnimator get() = ObjectAnimator.ofFloat(
    /* target = */ this,
    /* propertyName = */ "alpha",
    /* ...values = */ context.resources.btnAlpha
)

private val MaterialButton.hoveredCornerRadiusAnimator get() = ObjectAnimator.ofInt(
    /* target = */ this,
    /* propertyName = */ "cornerRadius",
    /* ...values = */ context.resources.btnHoveredCornerRadius
)

private val MaterialButton.pressedCornerRadiusAnimator get() = ObjectAnimator.ofInt(
    /* target = */ this,
    /* propertyName = */ "cornerRadius",
    /* ...values = */ context.resources.btnPressedCornerRadius
)

private val MaterialButton.defaultCornerRadiusAnimator get() = ObjectAnimator.ofInt(
    /* target = */ this,
    /* propertyName = */ "cornerRadius",
    /* ...values = */ context.resources.btnCornerRadius
)

private val MaterialButton.hoveredTranslationZAnimator get() = ObjectAnimator.ofFloat(
    /* target = */ this,
    /* propertyName = */ "translationZ",
    /* ...values = */ context.resources.btnHoveredTranslationZ
)

private val MaterialButton.pressedTranslationZAnimator get() = ObjectAnimator.ofFloat(
    /* target = */ this,
    /* propertyName = */ "translationZ",
    /* ...values = */ context.resources.btnPressedTranslationZ
)

private val MaterialButton.defaultTranslationZAnimator get() = ObjectAnimator.ofFloat(
    /* target = */ this,
    /* propertyName = */ "translationZ",
    /* ...values = */ context.resources.btnTranslationZ
)

internal val MaterialButton.hoveredAnimatorSet get() = AnimatorSet().apply {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        playTogether(hoveredAlphaAnimator, hoveredCornerRadiusAnimator)
    } else {
        playTogether(hoveredAlphaAnimator, hoveredCornerRadiusAnimator, hoveredTranslationZAnimator)
    }

    duration = context.resources.btnAnimDuration
}

internal val MaterialButton.pressedAnimatorSet get() = AnimatorSet().apply {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        playTogether(pressedAlphaAnimator, pressedCornerRadiusAnimator)
    } else {
        playTogether(pressedAlphaAnimator, pressedCornerRadiusAnimator, pressedTranslationZAnimator)
    }

    duration = context.resources.btnAnimDuration
}

internal val MaterialButton.defaultAnimatorSet get() = AnimatorSet().apply {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        playTogether(defaultAlphaAnimator, defaultCornerRadiusAnimator)
    } else {
        playTogether(defaultAlphaAnimator, defaultCornerRadiusAnimator, defaultTranslationZAnimator)
    }

    duration = context.resources.btnAnimDuration
}
