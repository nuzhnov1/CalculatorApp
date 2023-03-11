package com.sunman.feature.main

// TODO: update test
/*
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private val calculationStringObserver = Observer<String> {}
    private val calculationResultObserver = Observer<CalculationResult> {}

    @Before
    fun beforeEach() {
        val initialState = mapOf(MainViewModel.CALCULATION_STRING to "")
        val savedStateHandle = SavedStateHandle(initialState)

        viewModel = MainViewModel(savedStateHandle).apply {
            calculationField.observeForever(calculationStringObserver)
            resultField.observeForever(calculationResultObserver)
        }
    }

    @After
    fun afterEach() {
        viewModel.apply {
            calculationField.removeObserver(calculationStringObserver)
            resultField.removeObserver(calculationResultObserver)
        }
    }

    @Test
    fun `Testing the execution of simple expressions`() {
        println("\tTesting the execution of simple expressions...")

        viewModel.addItemToCalculationField("1")
        testExecution("1", "1")
        viewModel.addItemToCalculationField(".")
        viewModel.addItemToCalculationField("2")
        testExecution("1.2", "1.2")
        viewModel.removeOneItemFromCalculationString()
        viewModel.addItemToCalculationField("3")
        viewModel.addItemToCalculationField("*")
        viewModel.addItemToCalculationField("2")
        viewModel.addItemToCalculationField("+")
        viewModel.addItemToCalculationField("1")
        viewModel.addItemToCalculationField("1")
        viewModel.addItemToCalculationField("-")
        viewModel.addItemToCalculationField("1")
        viewModel.addItemToCalculationField("2")
        viewModel.removeOneItemFromCalculationString()
        viewModel.addItemToCalculationField("1")
        testExecution("1.3*2+11-11", "2.6")
        viewModel.executeCalculation()
        testExecution("2.6", "2.6")

        viewModel.addItemToCalculationField("÷")
        viewModel.addItemToCalculationField("()")
        viewModel.addItemToCalculationField("2")
        testExecution("2.6÷(2", "1.3")
        viewModel.executeCalculation()
        testExecution("1.3", "1.3")

        viewModel.addItemToCalculationField("%")
        testExecution("1.3%", "0.013")
        viewModel.removeOneItemFromCalculationString()
        testExecution("1.3", "1.3")
        viewModel.removeOneItemFromCalculationString()
        testExecution("1.", "1")
        viewModel.removeOneItemFromCalculationString()
        testExecution("1", "1")
        viewModel.removeOneItemFromCalculationString()
        testExecution("", "")

        viewModel.addItemToCalculationField("2")
        viewModel.addItemToCalculationField("^")
        viewModel.addItemToCalculationField("10")
        testExecution("2^10", "1024")
        viewModel.clearInputString()

        viewModel.addItemToCalculationField("√(")
        viewModel.addItemToCalculationField("1024")
        viewModel.addItemToCalculationField("()")
        testExecution("√(1024)", "32")
        viewModel.clearInputString()

        viewModel.addItemToCalculationField("3")
        viewModel.addItemToCalculationField("!")
        viewModel.addItemToCalculationField("!")
        testExecution("3!!", "720")
        viewModel.clearInputString()

        println("\tTesting the execution of simple expressions... OK")
    }

    @Test
    fun `Testing the execution of expressions with functions call`() {
        println("\tTesting the execution of expressions with functions call...")

        viewModel.addItemToCalculationField("2")
        viewModel.addItemToCalculationField("sin(")
        viewModel.addItemToCalculationField("π")
        testExecution("2sin(π", "0")
        viewModel.addItemToCalculationField("÷")
        viewModel.addItemToCalculationField("2")
        viewModel.addItemToCalculationField("()")
        testExecution("2sin(π÷2)", "2")
        repeat(5) { viewModel.removeOneItemFromCalculationString() }
        testExecution("2", "2")
        viewModel.removeOneItemFromCalculationString()
        testExecution("", "")

        viewModel.addItemToCalculationField("root(")
        viewModel.addItemToCalculationField("16")
        viewModel.addItemToCalculationField(",")
        viewModel.addItemToCalculationField("2")
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
        viewModel.addItemToCalculationField("sin(")
        viewModel.addItemToCalculationField("9")
        viewModel.addItemToCalculationField("0")
        testExecution("sin(90", "1")
        viewModel.executeCalculation()
        testExecution("1", "1")
        viewModel.clearInputString()

        viewModel.setAngleUnit(AngleUnit.GRADIAN)
        viewModel.addItemToCalculationField("cos(")
        viewModel.addItemToCalculationField("2")
        viewModel.addItemToCalculationField("0")
        viewModel.addItemToCalculationField("0")
        viewModel.addItemToCalculationField(")")
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
        viewModel.addItemToCalculationField("()")
        viewModel.addItemToCalculationField("e")
        testExecution("(e", "2.71828182845905")
        viewModel.removeOneItemFromCalculationString()
        viewModel.addItemToCalculationField("()")
        viewModel.addItemToCalculationField("π")
        testExecution("((π", "3.14159265358979")
        viewModel.addItemToCalculationField("()")
        testExecution("((π)", "3.14159265358979")
        viewModel.addItemToCalculationField("()")
        viewModel.addItemToCalculationField("()")
        viewModel.addItemToCalculationField("0")
        testExecution("((π))(0", "0")
        viewModel.addItemToCalculationField("()")
        testExecution("((π))(0)", "0")
        repeat(100) { viewModel.removeOneItemFromCalculationString() }
        println("\tTesting adding and removal brackets... OK")
    }

    private fun testExecution(expectedString: String, expectedResult: String) {
        val result = viewModel.resultField.value.toString()


        assertEquals(
            "The actual calculation string is not equal to the expected one",
            expectedString, viewModel.calculationField.value
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
            println("Testing methods of the MainViewModel class...")
        }

        @AfterClass
        @JvmStatic
        fun finish() {
            println("Testing methods of the MainViewModel class... OK")
        }
    }
}
*/
