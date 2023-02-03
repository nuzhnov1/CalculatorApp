package com.sunman.feature.main.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sunman.feature.main.R
import com.sunman.feature.main.databinding.InversionMainSubpanelBinding
import com.sunman.feature.main.databinding.MainFragmentBinding
import com.sunman.feature.main.databinding.MainSubpanelBinding
import com.sunman.feature.main.presentation.HISTORY_FRAGMENT_MIN_HEIGHT
import com.sunman.feature.main.presentation.MainViewModel
import com.sunman.feature.main.ui.animation.getAnimatorOfReplaceView
import com.sunman.feature.main.ui.animation.getAnimatorOfVerticalCollapse
import com.sunman.feature.main.ui.animation.getAnimatorOfVerticalExpand
import com.sunman.feature.main.ui.animation.setAnimationsOnAllButtons
import com.sunman.libcalculator.AngleUnit

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var isFunctionSubpanelVisible = false
    private var isInverseFunctionsUsed = false

    private val viewModel: MainViewModel by viewModels()


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

        binding.toolbar.apply {
            inflateMenu(R.menu.toolbar_menu)
            setOnMenuItemClickListener(::onMenuItemClicked)
        }

        binding.calculationPanel.apply {
            expressionSubpanel.fragment = this@MainFragment
            expressionSubpanel.viewModel = viewModel
            mainSubpanel.fragment = this@MainFragment
            mainSubpanel.viewModel = viewModel
            mainSubpanel.functionsSubpanel?.fragment = this@MainFragment
            mainSubpanel.functionsSubpanel?.viewModel = viewModel
        }

        hideToolbarOnSmallLandscapeScreens()
        hideHistoryFragmentOnSmallScreens()
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

            val functionSubpanelAnimator = if (isFunctionSubpanelVisible) {
                getAnimatorOfVerticalExpand(
                    LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    )
                )
            } else {
                getAnimatorOfVerticalCollapse()
            }

            functionSubpanelAnimator.apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.historyFragment.getAnimatorOfHistoryFragment()?.start()
                    }
                })

                start()
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

            getAnimatorOfReplaceView(newView) {
                parent.removeViewAt(index)
                parent.addView(newView, index)
            }.start()
        }
    }

    fun addButtonTextToInputString(view: View) {
        val buttonText = (view as Button).text.toString()
        viewModel.addItemToCalculationString(buttonText.toDisplayableRepresentation())
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

    private fun hideToolbarOnSmallLandscapeScreens() {
        val activity = requireActivity()
        val resources = activity.resources
        val orientation = resources.configuration.orientation
        val windowHeight = resources.displayMetrics.heightPixels.toDP(activity)

        if ((orientation == Configuration.ORIENTATION_LANDSCAPE) &&
            (windowHeight < 360 || windowHeight in 400..479)) {

            binding.toolbar.visibility = View.GONE
        }
    }

    private fun hideHistoryFragmentOnSmallScreens() {
        val windowHeight = binding.historyFragment.height.toDP(requireActivity())

        if (windowHeight < 150) {
            binding.historyFragment.visibility = View.GONE
        }
    }

    private fun View.getAnimatorOfHistoryFragment(): ValueAnimator? {
        val height = height.toDP(requireActivity())

        return when {
            visibility == View.VISIBLE && height < HISTORY_FRAGMENT_MIN_HEIGHT -> {
                getAnimatorOfVerticalCollapse()
            }

            visibility == View.GONE && height >= HISTORY_FRAGMENT_MIN_HEIGHT -> {
                getAnimatorOfVerticalExpand(
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                )
            }

            else -> null
        }
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
