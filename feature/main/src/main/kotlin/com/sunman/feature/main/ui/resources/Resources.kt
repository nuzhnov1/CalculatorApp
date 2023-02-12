package com.sunman.feature.main.ui.resources

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.sunman.feature.main.R


internal val Resources.historyFragmentMinHeight get() =
    getDimension(R.dimen.history_fragment_min_height)

internal val Resources.btnAlpha get() = TypedValue()
    .apply { getValue(R.dimen.btn_alpha, this, false) }
    .float

internal val Resources.btnHoveredAlpha get() =  TypedValue()
    .apply { getValue(R.dimen.btn_hovered_alpha, this, false) }
    .float

internal val Resources.btnPressedAlpha get() =  TypedValue()
    .apply { getValue(R.dimen.btn_pressed_alpha, this, false) }
    .float

internal val Resources.btnCornerRadius get() =
    getDimension(R.dimen.btn_corner_radius).toInt()

internal val Resources.btnHoveredCornerRadius get() =
    getDimension(R.dimen.btn_hovered_corner_radius).toInt()

internal val Resources.btnPressedCornerRadius get() =
    getDimension(R.dimen.btn_pressed_corner_radius).toInt()

internal val Resources.btnTranslationZ get() = getDimension(R.dimen.btn_translationZ)
internal val Resources.btnHoveredTranslationZ get() = getDimension(R.dimen.btn_hovered_translationZ)
internal val Resources.btnPressedTranslationZ get() = getDimension(R.dimen.btn_pressed_translationZ)
internal val Resources.btnAnimDuration get() = getInteger(R.integer.btn_anim_duration).toLong()
internal val Resources.animDuration get() = getInteger(R.integer.anim_duration).toLong()

@get:ColorInt internal val Context.transparentColor get() =
    ResourcesCompat.getColor(resources, android.R.color.transparent, theme)

internal val Resources.rad get() = getString(R.string.rad)
internal val Resources.deg get() = getString(R.string.deg)
internal val Resources.grad get() = getString(R.string.grad)

internal val Resources.power get() = getString(R.string.power)
internal val Resources.powerDescription get() = getString(R.string.power_description)
internal val Resources.sqrt get() = getString(R.string.sqrt)
internal val Resources.sqrtDescription get() = getString(R.string.sqrt_description)
internal val Resources.log get() = getString(R.string.log)
internal val Resources.logDescription get() = getString(R.string.log_description)
internal val Resources.sqr get() = getString(R.string.sqr)
internal val Resources.sqrDescription get() = getString(R.string.sqr_description)
internal val Resources.sin get() = getString(R.string.sin)
internal val Resources.sinDescription get() = getString(R.string.sin_description)
internal val Resources.cos get() = getString(R.string.cos)
internal val Resources.cosDescription get() = getString(R.string.cos_description)
internal val Resources.tan get() = getString(R.string.tan)
internal val Resources.tanDescription get() = getString(R.string.tan_description)
internal val Resources.cot get() = getString(R.string.cot)
internal val Resources.cotDescription get() = getString(R.string.cot_description)
internal val Resources.sec: String get() = getString(R.string.sec)
internal val Resources.secDescription get() = getString(R.string.sec_description)
internal val Resources.cosec get() = getString(R.string.cosec)
internal val Resources.cosecDescription get() = getString(R.string.cosec_description)
internal val Resources.asin get() = getString(R.string.asin)
internal val Resources.asinDescription get() = getString(R.string.asin_description)
internal val Resources.acos get() = getString(R.string.acos)
internal val Resources.acosDescription get() = getString(R.string.acos_description)
internal val Resources.atan get() = getString(R.string.atan)
internal val Resources.atanDescription get() = getString(R.string.atan_description)
internal val Resources.acot get() = getString(R.string.acot)
internal val Resources.acotDescription get() = getString(R.string.acot_description)
internal val Resources.asec: String get() = getString(R.string.asec)
internal val Resources.asecDescription get() = getString(R.string.asec_description)
internal val Resources.acosec get() = getString(R.string.acosec)
internal val Resources.acosecDescription get() = getString(R.string.acosec_description)
internal val Resources.sinh get() = getString(R.string.sinh)
internal val Resources.sinhDescription get() = getString(R.string.sinh_description)
internal val Resources.cosh get() = getString(R.string.cosh)
internal val Resources.coshDescription get() = getString(R.string.cosh_description)
internal val Resources.tanh get() = getString(R.string.tanh)
internal val Resources.tanhDescription get() = getString(R.string.tanh_description)
internal val Resources.coth get() = getString(R.string.coth)
internal val Resources.cothDescription get() = getString(R.string.coth_description)
internal val Resources.sech: String get() = getString(R.string.sech)
internal val Resources.sechDescription get() = getString(R.string.sech_description)
internal val Resources.csch get() = getString(R.string.csch)
internal val Resources.cschDescription get() = getString(R.string.csch_description)
internal val Resources.asinh get() = getString(R.string.asinh)
internal val Resources.asinhDescription get() = getString(R.string.asinh_description)
internal val Resources.acosh get() = getString(R.string.acosh)
internal val Resources.acoshDescription get() = getString(R.string.acosh_description)
internal val Resources.atanh get() = getString(R.string.atanh)
internal val Resources.atanhDescription get() = getString(R.string.atanh_description)
internal val Resources.acoth get() = getString(R.string.acoth)
internal val Resources.acothDescription get() = getString(R.string.acoth_description)
internal val Resources.asech: String get() = getString(R.string.asech)
internal val Resources.asechDescription get() = getString(R.string.asech_description)
internal val Resources.acsch get() = getString(R.string.acsch)
internal val Resources.acschDescription get() = getString(R.string.acsch_description)
internal val Resources.ln get() = getString(R.string.ln)
internal val Resources.lnDescription get() = getString(R.string.ln_description)
internal val Resources.log2 get() = getString(R.string.log2)
internal val Resources.log2Description get() = getString(R.string.log2_description)
internal val Resources.lg get() = getString(R.string.lg)
internal val Resources.lgDescription get() = getString(R.string.lg_description)
internal val Resources.exp get() = getString(R.string.exp)
internal val Resources.expDescription get() = getString(R.string.exp_description)
internal val Resources.powerOf2 get() = getString(R.string.power_of_2)
internal val Resources.powerOf2Description get() = getString(R.string.power_of_2_description)
internal val Resources.powerOf10 get() = getString(R.string.power_of_10)
internal val Resources.powerOf10Description get() = getString(R.string.power_of_10_description)
internal val Resources.cbrt get() = getString(R.string.cbrt)
internal val Resources.cbrtDescription get() = getString(R.string.cbrt_description)
internal val Resources.cube get() = getString(R.string.cube)
internal val Resources.cubeDescription get() = getString(R.string.cube_description)

internal val Resources.invalidExpression get() = getString(R.string.invalid_expression)
internal val Resources.divisionByZero get() = getString(R.string.division_by_zero)
internal val Resources.invalidArgument get() = getString(R.string.invalid_argument)
internal val Resources.invalidArgumentsNumber get() = getString(R.string.invalid_arguments_number)
internal val Resources.genericError get() = getString(R.string.generic_error)


internal fun Context.getArrowAVD(direction: ArrowDirection) = when (direction) {
    ArrowDirection.UP -> AnimatedVectorDrawableCompat.create(
        /* context = */ this,
        /* resId = */ R.drawable.ic_baseline_keyboard_arrow_up_24
    )

    ArrowDirection.DOWN -> AnimatedVectorDrawableCompat.create(
        /* context = */ this,
        /* resId = */ R.drawable.ic_baseline_keyboard_arrow_down_24
    )
}
