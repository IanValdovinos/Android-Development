package learnprogramming.academy.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import java.lang.Exception

import kotlinx.android.synthetic.main.activity_main.*

const val PENDING_OPERATION = "pendingOperation"
const val OPERAND1 = "operand1"
const val OPERAND1_STORED = "operand1 stored"

class MainActivity : AppCompatActivity() {

    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }


        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try{
                val value:Double = newNumber.text.toString().toDouble()
                performOperation(value, op)
            }catch (e: Exception){
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = op
        }

        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMultipy.setOnClickListener(opListener)
        buttonDevide.setOnClickListener(opListener)
        buttonEquals.setOnClickListener (opListener)

        val negNumber = View.OnClickListener {
            if (newNumber.text.toString() != "" && newNumber.text.toString() != "-" ){
                val number = newNumber.text.toString().toDouble()
                val inverseNumber = number.times(-1)
                newNumber.setText(inverseNumber.toString())
            } else if (newNumber.text.toString() == "-"){
                newNumber.setText("")
            } else {
                newNumber.append("-")
            }

        }

        negButton.setOnClickListener (negNumber)

        val clearCalculator = View.OnClickListener {
            operand1 = null
            pendingOperation ="="

            result.text.clear()
            newNumber.text.clear()
            operation.text = ""
        }

        clearButton.setOnClickListener (clearCalculator)
    }


    private fun performOperation(value: Double, operation: String) {
        if(operand1 == null){
            operand1 = value
        } else{

            if (pendingOperation == "="){
                pendingOperation = operation
            }

            operand1 = when(pendingOperation){
                "=" -> value
                "+" -> operand1!! + value
                "-" -> operand1!! - value
                "*" -> operand1!! * value
                "/" -> if (value == 0.0) Double.NaN else operand1!! / value
                else -> 0.0
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState.getString(PENDING_OPERATION, "")
        operation.text = pendingOperation

        operand1 = if(savedInstanceState.getBoolean(OPERAND1_STORED, false)){
            savedInstanceState.getDouble(OPERAND1)
        } else {
            null
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PENDING_OPERATION, pendingOperation)
        operand1?.let {
            outState.putDouble(OPERAND1, it)
            outState.putBoolean(OPERAND1_STORED, true)
        }
    }
}
