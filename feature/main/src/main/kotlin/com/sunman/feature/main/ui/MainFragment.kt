package com.sunman.feature.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.sunman.feature.main.R
import com.sunman.feature.main.databinding.MainFragmentBinding
import com.sunman.feature.main.presentation.MainViewModel
import com.sunman.feature.main.ui.animations.*
import com.sunman.libcalculator.AngleUnit

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    internal val binding get() = _binding!!

    private var isFunctionSubpanelVisible = false
    private var isInverseFunctionsUsed = false

    private val viewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(
            /* inflater = */ inflater,
            /* parent = */ container,
            /* attachToParent = */ false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.apply {
            inflateMenu(R.menu.toolbar_menu)
            setOnMenuItemClickListener(::onMenuItemClicked)
        }

        binding.apply {
            expressionSubpanel.lifecycleOwner = viewLifecycleOwner
            mainSubpanel.lifecycleOwner = viewLifecycleOwner
            mainSubpanel.functionsSubpanel?.lifecycleOwner = viewLifecycleOwner

            expressionSubpanel.fragment = this@MainFragment
            expressionSubpanel.viewModel = viewModel
            mainSubpanel.fragment = this@MainFragment
            mainSubpanel.viewModel = viewModel
            mainSubpanel.functionsSubpanel?.fragment = this@MainFragment
            mainSubpanel.functionsSubpanel?.viewModel = viewModel
        }

        binding.historyFragment.doOnLayout {
            binding.historyFragment.changeVisibilityImmediately()
            binding.historyFragment.addOnLayoutChangeListener(HistoryFragmentOnLayoutChangeListener)
        }

        setAnimationsOnAllMaterialButtons()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            isFunctionSubpanelVisible = savedInstanceState.getBoolean(IS_FUNCTION_SUBPANEL_SHOWN)
            isInverseFunctionsUsed = savedInstanceState.getBoolean(IS_USING_INVERSE_FUNCTIONS)
        }
    }

    override fun onStart() {
        super.onStart()

        if (isFunctionSubpanelVisible) {
            binding.mainSubpanel.functionsSubpanel?.root?.visibility = View.VISIBLE
        } else {
            binding.mainSubpanel.functionsSubpanel?.root?.visibility = View.GONE
        }

        if (isInverseFunctionsUsed) {
            binding.mainSubpanel.switchToInversionModeImmediately()
        } else {
            binding.mainSubpanel.switchToNormalModeImmediately()
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

    fun onChangeAngleUnit() {
        val newUnit = when (viewModel.angleUnit.value) {
            AngleUnit.RADIAN -> AngleUnit.DEGREE
            AngleUnit.DEGREE -> AngleUnit.GRADIAN
            else -> AngleUnit.RADIAN
        }

        viewModel.setAngleUnit(newUnit)
    }

    fun onAddButtonTextToInputString(view: View) {
        val buttonText = (view as MaterialButton).text.toString()
        viewModel.addItemToCalculationString(buttonText.toDisplayableString())
    }

    fun onLongClickToBackButton(): Boolean {
        viewModel.clearInputString()
        return true
    }

    fun onChangeUsingInverseFunctions() {
        isInverseFunctionsUsed = !isInverseFunctionsUsed

        if (isInverseFunctionsUsed) {
            binding.mainSubpanel.animateSwitchToInversionMode()
        } else {
            binding.mainSubpanel.animateSwitchToNormalMode()
        }
    }

    fun onChangeShowingFunctionsSubpanel() {
        isFunctionSubpanelVisible = !isFunctionSubpanelVisible

        if (isFunctionSubpanelVisible) {
            binding.mainSubpanel.functionsSubpanel?.animateExpand()
        } else {
            binding.mainSubpanel.functionsSubpanel?.animateCollapse()
        }
    }

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.functionsMenuItem -> {
            // TODO: implemented navigate to functions fragment
            true
        }

        R.id.historyMenuItem -> {
            // TODO: implemented navigate to history fragment
            true
        }

        R.id.settingsMenuItem -> {
            // TODO: implemented navigate to settings fragment
            true
        }

        R.id.aboutMenuItem -> {
            // TODO: implemented navigate to about fragment
            true
        }

        else -> false
    }


    private companion object {
        const val IS_FUNCTION_SUBPANEL_SHOWN = "isFunctionSubpanelFolded"
        const val IS_USING_INVERSE_FUNCTIONS = "isUsingInverseFunctions"
    }
}
