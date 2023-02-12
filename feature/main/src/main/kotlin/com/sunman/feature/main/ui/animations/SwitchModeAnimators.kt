package com.sunman.feature.main.ui.animations

import android.animation.*
import com.google.android.material.button.MaterialButton
import com.sunman.feature.main.databinding.FunctionsSubpanelBinding
import com.sunman.feature.main.databinding.MainSubpanelBinding
import com.sunman.feature.main.ui.resources.*


internal val MainSubpanelBinding.normalModeAnimator get() = root.context.resources.run {
    val animatorsList = mutableListOf(
        powerButton.getReplaceTextAnimator(power, powerDescription),
        sqrtButton.getReplaceTextAnimator(sqrt, sqrtDescription),
    )

    functionsSubpanel?.normalModeAnimators?.apply { animatorsList.addAll(this) }
    animatorSetOf(animatorsList)
}

internal val MainSubpanelBinding.inversionModeAnimator get() = root.context.resources.run {
    val animatorsList = mutableListOf(
        powerButton.getReplaceTextAnimator(log, logDescription),
        sqrtButton.getReplaceTextAnimator(sqr, sqrDescription),
    )

    functionsSubpanel?.inversionModeAnimators?.apply { animatorsList.addAll(this) }
    animatorSetOf(animatorsList)
}

private val FunctionsSubpanelBinding.normalModeAnimators get() = root.context.resources.run {
    listOfNotNull(
        sinButton.getReplaceTextAnimator(sin, sinDescription),
        cosButton.getReplaceTextAnimator(cos, cosDescription),
        tanButton.getReplaceTextAnimator(tan, tanDescription),
        cotButton.getReplaceTextAnimator(cot, cotDescription),
        secButton?.getReplaceTextAnimator(sec, secDescription),
        cosecButton?.getReplaceTextAnimator(cosec, cosecDescription),
        sinhButton?.getReplaceTextAnimator(sinh, sinhDescription),
        coshButton?.getReplaceTextAnimator(cosh, coshDescription),
        tanhButton?.getReplaceTextAnimator(tanh, tanhDescription),
        cothButton?.getReplaceTextAnimator(coth, cothDescription),
        sechButton?.getReplaceTextAnimator(sech, sechDescription),
        cschButton?.getReplaceTextAnimator(csch, cschDescription),
        lnButton.getReplaceTextAnimator(ln, lnDescription),
        log2Button.getReplaceTextAnimator(log2, log2Description),
        lgButton.getReplaceTextAnimator(lg, lgDescription),
        cbrtButton?.getReplaceTextAnimator(cbrt, cbrtDescription),
    )
}

private val FunctionsSubpanelBinding.inversionModeAnimators get() = root.context.resources.run {
    listOfNotNull(
        sinButton.getReplaceTextAnimator(asin, asinDescription),
        cosButton.getReplaceTextAnimator(acos, acosDescription),
        tanButton.getReplaceTextAnimator(atan, atanDescription),
        cotButton.getReplaceTextAnimator(acot, acotDescription),
        secButton?.getReplaceTextAnimator(asec, asecDescription),
        cosecButton?.getReplaceTextAnimator(acosec, acosecDescription),
        sinhButton?.getReplaceTextAnimator(asinh, asinhDescription),
        coshButton?.getReplaceTextAnimator(acosh, acoshDescription),
        tanhButton?.getReplaceTextAnimator(atanh, atanhDescription),
        cothButton?.getReplaceTextAnimator(acoth, acothDescription),
        sechButton?.getReplaceTextAnimator(asech, asechDescription),
        cschButton?.getReplaceTextAnimator(acsch, acschDescription),
        lnButton.getReplaceTextAnimator(exp, expDescription),
        log2Button.getReplaceTextAnimator(powerOf2, powerOf2Description),
        lgButton.getReplaceTextAnimator(powerOf10, powerOf10Description),
        cbrtButton?.getReplaceTextAnimator(cube, cubeDescription)
    )
}


private fun MaterialButton.getReplaceTextAnimator(
    newText: String,
    newContentDescription: String
): AnimatorSet = context.resources.run {
    val textColor = currentTextColor
    val transparentColor = context.transparentColor

    val hideOldTextAnimator = ObjectAnimator.ofObject(
        /* target = */ this@getReplaceTextAnimator,
        /* propertyName = */ "textColor",
        /* evaluator = */ ArgbEvaluator(),
        /* ...values = */ textColor, transparentColor
    ).apply { duration = animDuration / 2 }

    val showNewTextAnimator = ObjectAnimator.ofObject(
        /* target = */ this@getReplaceTextAnimator,
        /* propertyName = */ "textColor",
        /* evaluator = */ ArgbEvaluator(),
        /* ...values = */ transparentColor, textColor
    ).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                text = newText
                contentDescription = newContentDescription
            }
        })
        duration = animDuration / 2
    }

    AnimatorSet().apply {
        playSequentially(hideOldTextAnimator, showNewTextAnimator)
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) { isEnabled = false }
            override fun onAnimationEnd(animation: Animator) { isEnabled = true }
            override fun onAnimationCancel(animation: Animator) { isEnabled = true }
        })
    }
}

private fun MainSubpanelBinding.animatorSetOf(animators: Collection<Animator>) =
    AnimatorSet().apply {
        playTogether(*animators.toTypedArray())
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) { inversionButton.isEnabled = false }
            override fun onAnimationEnd(animation: Animator) { inversionButton.isEnabled = true }
            override fun onAnimationCancel(animation: Animator) { inversionButton.isEnabled = true }
        })
    }
