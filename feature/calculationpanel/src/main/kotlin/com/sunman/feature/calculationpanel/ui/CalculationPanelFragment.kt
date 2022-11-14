package com.sunman.feature.calculationpanel.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sunman.feature.calculationpanel.R
import com.sunman.feature.calculationpanel.databinding.FragmentCalculationPanelBinding
import com.sunman.feature.calculationpanel.presentation.CalculationPanelViewModel
import com.sunman.feature.calculationpanel.presentation.toInternalRepresentation

class CalculationPanelFragment : Fragment() {

    private var _binding: FragmentCalculationPanelBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalculationPanelViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculationPanelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.isFunctionSubpanelShown.observe(viewLifecycleOwner) { isShow ->
            showFunctionsSubpanel(isShow)
        }
        viewModel.isUseInverseFunctions.observe(viewLifecycleOwner) { isUse ->
            useInverseFunctions(isUse)
        }

        setListenersOnUpPanel()
        setListenersOnMainSubpanelButtons()
        setListenersOnFunctionsSubpanelButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListenersOnUpPanel() {
        binding.apply {
            collapseButton.setOnClickListener { viewModel.changeFunctionSubpanel() }
            powerButton.setOnClickListener { addButtonTextToInputString(it) }
            sqrtButton.setOnClickListener { addButtonTextToInputString(it) }
            factorialButton.setOnClickListener { addButtonTextToInputString(it) }
            inversionButton.setOnClickListener { viewModel.changeUseInverseFunctions() }
        }
    }

    private fun setListenersOnMainSubpanelButtons() {
        binding.mainSubpanel.apply {
            clearButton.setOnClickListener { viewModel.clearInputString() }
            bracketButton.setOnClickListener { viewModel.addBracket() }
            percentButton.setOnClickListener { addButtonTextToInputString(it) }
            divButton.setOnClickListener { addButtonTextToInputString(it) }
            sevenButton.setOnClickListener { addButtonTextToInputString(it) }
            eightButton.setOnClickListener { addButtonTextToInputString(it) }
            nineButton.setOnClickListener { addButtonTextToInputString(it) }
            mulButton.setOnClickListener { addButtonTextToInputString(it) }
            fourButton.setOnClickListener { addButtonTextToInputString(it) }
            fiveButton.setOnClickListener { addButtonTextToInputString(it) }
            sixButton.setOnClickListener { addButtonTextToInputString(it) }
            subButton.setOnClickListener { addButtonTextToInputString(it) }
            oneButton.setOnClickListener { addButtonTextToInputString(it) }
            twoButton.setOnClickListener { addButtonTextToInputString(it) }
            threeButton.setOnClickListener { addButtonTextToInputString(it) }
            addButton.setOnClickListener { addButtonTextToInputString(it) }
            zeroButton.setOnClickListener { addButtonTextToInputString(it) }
            dotButton.setOnClickListener { addButtonTextToInputString(it) }
            backButton.setOnClickListener { viewModel.removeOneCharFromInputString() }
            equalButton.setOnClickListener { viewModel.executeCalculation(requireContext()) }
        }
    }

    private fun setListenersOnFunctionsSubpanelButtons() {
        binding.functionsSubpanel.apply {
            sinButton.setOnClickListener { addButtonTextToInputString(it) }
            cosButton.setOnClickListener { addButtonTextToInputString(it) }
            tanButton.setOnClickListener { addButtonTextToInputString(it) }
            cotButton.setOnClickListener { addButtonTextToInputString(it) }
            lnButton.setOnClickListener { addButtonTextToInputString(it) }
            sinhButton.setOnClickListener { addButtonTextToInputString(it) }
            coshButton.setOnClickListener { addButtonTextToInputString(it) }
            tanhButton.setOnClickListener { addButtonTextToInputString(it) }
            cothButton.setOnClickListener { addButtonTextToInputString(it) }
            log2Button.setOnClickListener { addButtonTextToInputString(it) }
            log10Button.setOnClickListener { addButtonTextToInputString(it) }
            cbrtButton.setOnClickListener { addButtonTextToInputString(it) }
            rootButton.setOnClickListener { addButtonTextToInputString(it) }
            ceilButton.setOnClickListener { addButtonTextToInputString(it) }
            absButton.setOnClickListener { addButtonTextToInputString(it) }
            hypotButton.setOnClickListener { addButtonTextToInputString(it) }
            floorButton.setOnClickListener { addButtonTextToInputString(it) }
            roundButton.setOnClickListener { addButtonTextToInputString(it) }
            fractionButton.setOnClickListener { addButtonTextToInputString(it) }
            piButton.setOnClickListener { addButtonTextToInputString(it) }
            eButton.setOnClickListener { addButtonTextToInputString(it) }
        }
    }

    private fun showFunctionsSubpanel(isShow: Boolean) {
        if (isShow) {
            binding.functionsSubpanel.root.visibility = View.VISIBLE
        } else {
            binding.functionsSubpanel.root.visibility = View.GONE
        }
    }

    private fun useInverseFunctions(isUse: Boolean) {
        val context = requireContext()

        if (isUse) {
            binding.powerButton.text = context.getString(R.string.log)
            binding.functionsSubpanel.apply {
                sinButton.text = context.getString(R.string.asin)
                cosButton.text = context.getString(R.string.acos)
                tanButton.text = context.getString(R.string.atan)
                cotButton.text = context.getString(R.string.acot)
                lnButton.text = context.getString(R.string.exp)
                sinhButton.text = context.getString(R.string.asinh)
                coshButton.text = context.getString(R.string.acosh)
                tanhButton.text = context.getString(R.string.atanh)
                cothButton.text = context.getString(R.string.acoth)
                log2Button.text = context.getString(R.string.powerOf2)
                log10Button.text = context.getString(R.string.powerOf10)
            }
        } else {
            binding.powerButton.text = context.getString(R.string.power)
            binding.functionsSubpanel.apply {
                sinButton.text = context.getString(R.string.sin)
                cosButton.text = context.getString(R.string.cos)
                tanButton.text = context.getString(R.string.tan)
                cotButton.text = context.getString(R.string.cot)
                lnButton.text = context.getString(R.string.ln)
                sinhButton.text = context.getString(R.string.sinh)
                coshButton.text = context.getString(R.string.cosh)
                tanhButton.text = context.getString(R.string.tanh)
                cothButton.text = context.getString(R.string.coth)
                log2Button.text = context.getString(R.string.log2)
                log10Button.text = context.getString(R.string.log10)
            }
        }
    }

    private fun addButtonTextToInputString(view: View) =
        viewModel.addToInputString((view as Button).toInternalRepresentation())
}
