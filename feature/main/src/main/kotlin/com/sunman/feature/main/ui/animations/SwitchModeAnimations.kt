package com.sunman.feature.main.ui.animations

import com.sunman.feature.main.databinding.MainSubpanelBinding
import com.sunman.feature.main.ui.resources.*


internal fun MainSubpanelBinding.animateSwitchToNormalMode() = normalModeAnimator.start()
internal fun MainSubpanelBinding.animateSwitchToInversionMode() = inversionModeAnimator.start()

internal fun MainSubpanelBinding.switchToNormalModeImmediately(): Unit =
    root.context.resources.run {
        powerButton.text = power
        powerButton.contentDescription = powerDescription
        sqrtButton.text = sqrt
        sqrtButton.contentDescription = sqrtDescription

        functionsSubpanel?.run {
            sinButton.text = sin
            sinButton.contentDescription = sinDescription
            cosButton.text = cos
            cosButton.contentDescription = cosDescription
            tanButton.text = tan
            tanButton.contentDescription = tanDescription
            cotButton.text = cot
            cotButton.contentDescription = cotDescription
            secButton?.text = sec
            secButton?.contentDescription = secDescription
            cosecButton?.text = cosec
            cosecButton?.contentDescription = cosecDescription
            sinhButton?.text = sinh
            sinhButton?.contentDescription = sinhDescription
            coshButton?.text = cosh
            coshButton?.contentDescription = coshDescription
            tanhButton?.text = tanh
            tanhButton?.contentDescription = tanhDescription
            cothButton?.text = coth
            cothButton?.contentDescription = cothDescription
            sechButton?.text = sech
            sechButton?.contentDescription = sechDescription
            cschButton?.text = csch
            cschButton?.contentDescription = cschDescription
            lnButton.text = ln
            lnButton.contentDescription = lnDescription
            log2Button.text = log2
            log2Button.contentDescription = log2Description
            lgButton.text = lg
            lgButton.contentDescription = lgDescription
            cbrtButton?.text = cbrt
            cbrtButton?.contentDescription = cbrtDescription
        }
    }

internal fun MainSubpanelBinding.switchToInversionModeImmediately(): Unit =
    root.context.resources.run {
        powerButton.text = log
        powerButton.contentDescription = logDescription
        sqrtButton.text = sqr
        sqrtButton.contentDescription = sqrDescription

        functionsSubpanel?.run {
            sinButton.text = asin
            sinButton.contentDescription = asinDescription
            cosButton.text = acos
            cosButton.contentDescription = acosDescription
            tanButton.text = atan
            tanButton.contentDescription = atanDescription
            cotButton.text = acot
            cotButton.contentDescription = acotDescription
            secButton?.text = asec
            secButton?.contentDescription = asecDescription
            cosecButton?.text = acosec
            cosecButton?.contentDescription = acosecDescription
            sinhButton?.text = asinh
            sinhButton?.contentDescription = asinhDescription
            coshButton?.text = acosh
            coshButton?.contentDescription = acoshDescription
            tanhButton?.text = atanh
            tanhButton?.contentDescription = atanhDescription
            cothButton?.text = acoth
            cothButton?.contentDescription = acothDescription
            sechButton?.text = asech
            sechButton?.contentDescription = asechDescription
            cschButton?.text = acsch
            cschButton?.contentDescription = acschDescription
            lnButton.text = exp
            lnButton.contentDescription = expDescription
            log2Button.text = powerOf2
            log2Button.contentDescription = powerOf2Description
            lgButton.text = powerOf10
            lgButton.contentDescription = powerOf10Description
            cbrtButton?.text = cube
            cbrtButton?.contentDescription = cubeDescription
        }
    }
