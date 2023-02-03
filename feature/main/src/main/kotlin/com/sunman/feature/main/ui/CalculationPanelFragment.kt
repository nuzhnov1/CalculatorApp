package com.sunman.feature.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sunman.feature.main.databinding.InversionMainSubpanelBinding
import com.sunman.feature.main.databinding.MainFragmentBinding
import com.sunman.feature.main.databinding.MainSubpanelBinding
import com.sunman.feature.main.presentation.CalculationPanelViewModel
import com.sunman.feature.main.ui.animation.animateReplaceView
import com.sunman.feature.main.ui.animation.animateVerticalCollapse
import com.sunman.feature.main.ui.animation.animateVerticalExpand
import com.sunman.feature.main.ui.animation.setAnimationsOnAllButtons
import com.sunman.libcalculator.AngleUnit

class CalculationPanelFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var isFunctionSubpanelVisible = false
    private var isInverseFunctionsUsed = false

    private val viewModel: CalculationPanelViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.setAnimationsOnAllButtons()

        binding.calculationPanel.apply {
            expressionSubpanel.fragment = this@CalculationPanelFragment
            expressionSubpanel.viewModel = viewModel
            mainSubpanel.fragment = this@CalculationPanelFragment
            mainSubpanel.viewModel = viewModel
            mainSubpanel.functionsSubpanel?.fragment = this@CalculationPanelFragment
            mainSubpanel.functionsSubpanel?.viewModel = viewModel
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            isFunctionSubpanelVisible = savedInstanceState.getBoolean(IS_FUNCTION_SUBPANEL_SHOWN)
            isInverseFunctionsUsed = savedInstanceState.getBoolean(IS_USING_INVERSE_FUNCTIONS)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(IS_FUNCTION_SUBPANEL_SHOWN, isFunctionSubpanelVisible)
        outState.putBoolean(IS_USING_INVERSE_FUNCTIONS, isInverseFunctionsUsed)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun changeAngleUnit() {
        val newUnit = when (viewModel.angleUnit.value) {
            AngleUnit.RADIAN -> AngleUnit.DEGREE
            AngleUnit.DEGREE -> AngleUnit.GRADIAN
            else -> AngleUnit.RADIAN
        }

        viewModel.setAngleUnit(newUnit)
    }

    fun changeShowingFunctionsSubpanel() {
        binding.calculationPanel.mainSubpanel.functionsSubpanel?.root?.apply {
            isFunctionSubpanelVisible = !isFunctionSubpanelVisible

            if (isFunctionSubpanelVisible) {
                animateVerticalExpand(
                    LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    )
                )
            } else {
                animateVerticalCollapse()
            }
        }
    }

    fun changeUsingInverseFunctions() {
        binding.calculationPanel.mainSubpanel.root.apply {
            isInverseFunctionsUsed = !isInverseFunctionsUsed

            val parent = parent as ViewGroup
            val index = parent.indexOfChild(this)

            val newView = if (isInverseFunctionsUsed) {
                InversionMainSubpanelBinding
                    .inflate(layoutInflater, parent, false)
                    .root
            } else {
                MainSubpanelBinding
                    .inflate(layoutInflater, parent, false)
                    .root
            }

            animateReplaceView(newView) {
                parent.removeViewAt(index)
                parent.addView(newView, index)
            }
        }
    }

    fun addButtonTextToInputString(view: View) {
        val buttonText = (view as Button).text.toString()
        viewModel.addItemToCalculationString(buttonText.toDisplayableRepresentation())
    }

    private fun String.toDisplayableRepresentation() =
        when (val label = lowercase()) {
            in "0".."9", ".", "+", "-", "*", "÷", "%", "()", ",", "^", "!", "π", "e" -> label
            "2^x" -> "2^("
            "10^x" -> "10^("
            "x^2" -> "^2"
            "x^3" -> "^3"
            else -> "$label("
        }


    companion object {
        const val IS_FUNCTION_SUBPANEL_SHOWN = "isFunctionSubpanelFolded"
        const val IS_USING_INVERSE_FUNCTIONS = "isUsingInverseFunctions"
    }
}
