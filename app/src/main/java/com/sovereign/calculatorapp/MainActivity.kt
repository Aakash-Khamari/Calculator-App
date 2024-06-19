package com.sovereign.calculatorapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sovereign.calculatorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var firstValue = ""
    private var secondValue = ""
    private var answer = ""
    private var operator = ""
    private var currentText = ""
    private var isOperatorSelected = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    fun clickEvent(view: View) {
        when (view.id) {
            R.id.allClear -> clearAll()
            R.id.backspace -> backspace()
            R.id.percent -> onOperatorClick("%")
            R.id.divide -> onOperatorClick("\u00F7")
            R.id.multiply -> onOperatorClick("\u00d7")
            R.id.minus -> onOperatorClick("-")
            R.id.plus -> onOperatorClick("+")
            R.id.equals -> calculate()
            R.id.dot -> onDotClick()
            R.id.zero -> onDigitClick("0")
            R.id.doubleZero -> onDigitClick("00")
            R.id.one -> onDigitClick("1")
            R.id.two -> onDigitClick("2")
            R.id.three -> onDigitClick("3")
            R.id.four -> onDigitClick("4")
            R.id.five -> onDigitClick("5")
            R.id.six -> onDigitClick("6")
            R.id.seven -> onDigitClick("7")
            R.id.eight -> onDigitClick("8")
            R.id.nine -> onDigitClick("9")
        }
    }

    private fun updateDisplay(text: String) {
        currentText += text
        binding.resultTV.text = currentText
    }

    private fun hintDisplay(answer: String) {
        binding.displayTV.visibility = View.VISIBLE
        val doubleAnswer = answer.toDoubleOrNull()
        if (doubleAnswer != null) {
            var result = "="
            result+= doubleAnswer
            if (doubleAnswer % 1 == 0.0) {
                binding.displayTV.text = doubleAnswer.toInt().toString()
            } else {
                binding.displayTV.text = answer
            }
        } else {
            binding.displayTV.text = "Error"
        }
    }

    private fun hintCalculate(){
        if (firstValue.isNotEmpty() && isOperatorSelected) {
            val first = firstValue.toDoubleOrNull()
            var second = secondValue.toDoubleOrNull()

            if (first != null && second != null) {
                answer = when (operator) {
                    "+" -> (first + second).toString()
                    "-" -> (first - second).toString()
                    "\u00d7" -> (first * second).toString()
                    "\u00F7" -> if (second != 0.0) (first / second).toString() else "Error"
                    "%" -> (first % second).toString()
                    else -> ""
                }
                hintDisplay(answer)
                }else if (first != null && second == null){
                    answer = when (operator) {
                        "+" -> {
                            second = 0.0
                            (first + second).toString()
                        }

                        "-" -> {
                            second = 0.0
                            (first - second).toString()
                        }

                        "\u00d7" -> {
                            second = 1.0
                            (first * second).toString()
                        }

                        "\u00F7" -> if (second != 0.0) {
                            second = 1.0
                            (first / second).toString()
                        } else "Error"

                        "%" -> {
                            second = 1.0
                            (first % second).toString()
                        }

                        else -> ""
                    }
                    hintDisplay(answer)
            }
            else {
                    binding.displayTV.text = "Error"
                }
            }
        }

    private fun clearAll() {
        currentText = ""
        firstValue = ""
        secondValue = ""
        operator = ""
        answer = ""
        isOperatorSelected = false
        binding.resultTV.text = ""
        binding.displayTV.text = ""
        binding.displayTV.visibility = View.GONE
    }

    private fun onOperatorClick(op: String) {
        if (isOperatorSelected){
            currentText = currentText.dropLast(1)
            operator = op
            updateDisplay(op)
            binding.resultTV.text = currentText
        }else if (firstValue.isNotEmpty()) {
            operator = op
            isOperatorSelected = true
            updateDisplay(op)
        }
        hintCalculate()
    }

    private fun backspace() {
        if (!isOperatorSelected && firstValue.isNotEmpty()) {
            firstValue = firstValue.dropLast(1)
            currentText = currentText.dropLast(1)
            binding.resultTV.text = currentText
        } else if (isOperatorSelected && secondValue.isNotEmpty()) {
            secondValue = secondValue.dropLast(1)
            currentText = currentText.dropLast(1)
            binding.resultTV.text = currentText
            hintCalculate()
        }else if (isOperatorSelected && secondValue.isEmpty()) {
            currentText = currentText.dropLast(1)
            binding.resultTV.text = currentText
            binding.displayTV.text = ""
            binding.displayTV.visibility = View.GONE
        }
    }

    private fun onDigitClick(number: String) {
        if (!isOperatorSelected) {
            firstValue += number
            updateDisplay(number)
        } else {
            secondValue += number
            updateDisplay(number)
            hintCalculate()
        }
    }

    private fun onDotClick() {
        val dot = "."
        if (!isOperatorSelected && !firstValue.contains(".")) {
            firstValue += "."
            updateDisplay(dot)
        } else if (isOperatorSelected && !secondValue.contains(".")) {
            secondValue += "."
            updateDisplay(dot)
        }
    }

    private fun calculate() {
        binding.displayTV.visibility = View.GONE
        if (firstValue.isNotEmpty() && secondValue.isNotEmpty()) {
            val first = firstValue.toDoubleOrNull()
            val second = secondValue.toDoubleOrNull()

            if (first != null && second != null) {
                answer = when (operator) {
                    "+" -> (first + second).toString()
                    "-" -> (first - second).toString()
                    "\u00d7" -> (first * second).toString()
                    "\u00F7" -> if (second != 0.0) (first / second).toString() else "Error"
                    "%" -> (first % second).toString()
                    else -> ""
                }
                displayResult()
            } else {
                binding.resultTV.text = "Error"
            }
        }
    }

    private fun displayResult() {
        val doubleAnswer = answer.toDoubleOrNull()
        if (doubleAnswer != null) {
            if (doubleAnswer % 1 == 0.0) {
                binding.resultTV.text = doubleAnswer.toInt().toString()
            } else {
                binding.resultTV.text = answer
            }
        } else {
            binding.resultTV.text = "Error"
        }

        firstValue = ""
        secondValue = ""
        operator = ""
        answer = ""
        currentText = ""
        binding.displayTV.text  = ""
        isOperatorSelected = false
    }
}
