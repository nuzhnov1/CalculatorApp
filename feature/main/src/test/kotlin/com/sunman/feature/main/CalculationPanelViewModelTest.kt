package com.sunman.feature.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.sunman.feature.main.presentation.CalculationPanelViewModel
import com.sunman.libcalculator.AngleUnit
import com.sunman.libcalculator.CalculationResult
import org.junit.*
import org.junit.Assert.assertEquals

class CalculationPanelViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CalculationPanelViewModel
    private val calculationStringObserver = Observer<String> {}
    private val calculationResultObserver = Observer<CalculationResult> {}

    @Before
    fun beforeEach() {
        val initialState = mapOf(CalculationPanelViewModel.CALCULATION_STRING to "")
        val savedStateHandle = SavedStateHandle(initialState)

        viewModel = CalculationPanelViewModel(savedStateHandle).apply {
            calculationString.observeForever(calculationStringObserver)
            calculationResult.observeForever(calculationResultObserver)
        }
    }

    @After
    fun afterEach() {
        viewModel.apply {
            calculationString.removeObserver(calculationStringObserver)
            calculationResult.removeObserver(calculationResultObserver)
        }
    }

    @Test
    fun `Testing the execution of simple expressions`() {
        println("\tTesting the execution of simple expressions...")

        viewModel.addItemToCalculationString("1")
        testExecution("1", "1")
        viewModel.addItemToCalculationString(".")
        viewModel.addItemToCalculationString("2")
        testExecution("1.2", "1.2")
        viewModel.removeOneItemFromCalculationString()
        viewModel.addItemToCalculationString("3")
        viewModel.addItemToCalculationString("*")
        viewModel.addItemToCalculationString("2")
        viewModel.addItemToCalculationString("+")
        viewModel.addItemToCalculationString("1")
        viewModel.addItemToCalculationString("1")
        viewModel.addItemToCalculationString("-")
        viewModel.addItemToCalculationString("1")
        viewModel.addItemToCalculationString("2")
        viewModel.removeOneItemFromCalculationString()
        viewModel.addItemToCalculationString("1")
        testExecution("1.3*2+11-11", "2.6")
        viewModel.executeCalculation()
        testExecution("2.6", "2.6")

        viewModel.addItemToCalculationString("÷")
        viewModel.addItemToCalculationString("()")
        viewModel.addItemToCalculationString("2")
        testExecution("2.6÷(2", "1.3")
        viewModel.executeCalculation()
        testExecution("1.3", "1.3")

        viewModel.addItemToCalculationString("%")
        testExecution("1.3%", "0.013")
        viewModel.removeOneItemFromCalculationString()
        testExecution("1.3", "1.3")
        viewModel.removeOneItemFromCalculationString()
        testExecution("1.", "1")
        viewModel.removeOneItemFromCalculationString()
        testExecution("1", "1")
        viewModel.removeOneItemFromCalculationString()
        testExecution("", "")

        viewModel.addItemToCalculationString("2")
        viewModel.addItemToCalculationString("^")
        viewModel.addItemToCalculationString("10")
        testExecution("2^10", "1024")
        viewModel.clearInputString()

        viewModel.addItemToCalculationString("√(")
        viewModel.addItemToCalculationString("1024")
        viewModel.addItemToCalculationString("()")
        testExecution("√(1024)", "32")
        viewModel.clearInputString()

        viewModel.addItemToCalculationString("3")
        viewModel.addItemToCalculationString("!")
        viewModel.addItemToCalculationString("!")
        testExecution("3!!", "720")
        viewModel.clearInputString()

        println("\tTesting the execution of simple expressions... OK")
    }

    @Test
    fun `Testing the execution of expressions with functions call`() {
        println("\tTesting the execution of expressions with functions call...")

        viewModel.addItemToCalculationString("2")
        viewModel.addItemToCalculationString("sin(")
        viewModel.addItemToCalculationString("π")
        testExecution("2sin(π", "0")
        viewModel.addItemToCalculationString("÷")
        viewModel.addItemToCalculationString("2")
        viewModel.addItemToCalculationString("()")
        testExecution("2sin(π÷2)", "2")
        repeat(5) { viewModel.removeOneItemFromCalculationString() }
        testExecution("2", "2")
        viewModel.removeOneItemFromCalculationString()
        testExecution("", "")

        viewModel.addItemToCalculationString("root(")
        viewModel.addItemToCalculationString("16")
        viewModel.addItemToCalculationString(",")
        viewModel.addItemToCalculationString("2")
        testExecution("root(16,2", "4")
        repeat(5) { viewModel.removeOneItemFromCalculationString() }
        testExecution("", "")

        println("\tTesting the execution of expressions with functions call... OK")
    }

    @Test
    fun `Testing the execution of expressions with different angle measurement units`() {
        println(
            "\tTesting the execution of expressions with different angle measurement units..."
        )

        viewModel.setAngleUnit(AngleUnit.DEGREE)
        viewModel.addItemToCalculationString("sin(")
        viewModel.addItemToCalculationString("9")
        viewModel.addItemToCalculationString("0")
        testExecution("sin(90", "1")
        viewModel.executeCalculation()
        testExecution("1", "1")
        viewModel.clearInputString()

        viewModel.setAngleUnit(AngleUnit.GRADIAN)
        viewModel.addItemToCalculationString("cos(")
        viewModel.addItemToCalculationString("2")
        viewModel.addItemToCalculationString("0")
        viewModel.addItemToCalculationString("0")
        viewModel.addItemToCalculationString(")")
        testExecution("cos(200)", "-1")
        viewModel.executeCalculation()
        viewModel.clearInputString()

        println(
            "\tTesting the execution of expressions with different angle measurement units... OK"
        )
    }

    @Test
    fun `Testing adding and removal brackets`() {
        println("\tTesting adding and removal brackets...")
        viewModel.addItemToCalculationString("()")
        viewModel.addItemToCalculationString("e")
        testExecution("(e", "2.71828182845905")
        viewModel.removeOneItemFromCalculationString()
        viewModel.addItemToCalculationString("()")
        viewModel.addItemToCalculationString("π")
        testExecution("((π", "3.14159265358979")
        viewModel.addItemToCalculationString("()")
        testExecution("((π)", "3.14159265358979")
        viewModel.addItemToCalculationString("()")
        viewModel.addItemToCalculationString("()")
        viewModel.addItemToCalculationString("0")
        testExecution("((π))(0", "0")
        viewModel.addItemToCalculationString("()")
        testExecution("((π))(0)", "0")
        repeat(100) { viewModel.removeOneItemFromCalculationString() }
        println("\tTesting adding and removal brackets... OK")
    }

    private fun testExecution(expectedString: String, expectedResult: String) {
        val result = viewModel.calculationResult.value.toString()


        assertEquals(
            "The actual calculation string is not equal to the expected one",
            expectedString, viewModel.calculationString.value
        )
        assertEquals(
            "The actual calculation result is not equal to the expected one",
            expectedResult, result
        )
    }


    companion object {
        @BeforeClass
        @JvmStatic
        fun start() {
            println("Testing methods of the CalculationPanelViewModel class...")
        }

        @AfterClass
        @JvmStatic
        fun finish() {
            println("Testing methods of the CalculationPanelViewModel class... OK")
        }
    }
}
