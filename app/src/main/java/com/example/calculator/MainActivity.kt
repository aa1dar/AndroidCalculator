package com.example.calculator


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.db.DbManager
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private val TAG: String = "ERROR"
    private var isDotted: Boolean = false
    private val operators = listOf("+", "-", "*", "/")
    private var prevSym: String? = null

    val DbManager = DbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DbManager.open()

        tvSqrt.setOnClickListener { addSymbol("√") }
        tvOpen.setOnClickListener { addSymbol("(") }
        tvClose.setOnClickListener { addSymbol(")") }
        tvDevide.setOnClickListener { addSymbol("/") }
        tvMinus.setOnClickListener { addSymbol("-") }
        tvPlus.setOnClickListener { addSymbol("+") }
        tvMult.setOnClickListener { addSymbol("*") }
        tvOne.setOnClickListener { addSymbol("1") }
        tvTwo.setOnClickListener { addSymbol("2") }
        tvThree.setOnClickListener { addSymbol("3") }
        tvFour.setOnClickListener { addSymbol("4") }
        tvFive.setOnClickListener { addSymbol("5") }
        tvSix.setOnClickListener { addSymbol("6") }
        tvSeven.setOnClickListener { addSymbol("7") }
        tvEight.setOnClickListener { addSymbol("8") }
        tvNine.setOnClickListener { addSymbol("9") }
        tvZero.setOnClickListener { addSymbol("0") }
        tvDot.setOnClickListener { addSymbol(".") }
        tvCE.setOnLongClickListener { delAll() }
        tvCE.setOnClickListener { delOne() }
        tvAnswer.setOnClickListener { gotoAns() }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnHistory) {
            val i = Intent(this, History::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        DbManager.close()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("numField", numberField.text.toString())
            putString("resField", resultField.text.toString())
        }

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        numberField.text = savedInstanceState.getString("numField")
        resultField.text = savedInstanceState.getString("resField")
    }


    fun gotoAns(){
        val calc = Calculator(numberField.text.toString())
        try {
            val answer = calc.getAnswer()
            resultField.text = answer.toString()

            DbManager.insertToDb(numberField.text.toString() + "=", resultField.text.toString())

        }catch (e:Exception){
            resultField.text = ""
            numberField.text = "Ошибка"
            Log.d(TAG,e.toString())
        }


    }


    fun delAll():Boolean{
        numberField.text = ""
        resultField.text = ""
        prevSym = null
        isDotted = false
        return true
    }

    fun delOne(){
        var field = numberField.text.toString()
        if(field!="") {
            if(field[field.length-1].toString()==".")
                isDotted = false
            field = field.substring(0, field.length - 1)
            numberField.text = field
            if(field!="")
                prevSym= field[field.length-1].toString()
            else
                prevSym = null
        }

    }


    fun addSymbol(str:String){
        when {
            str in operators && prevSym in operators -> {
                delOne()

            }
            isDotted -> when (str) {
                                    "." -> return
                                    !in "0".."9" -> isDotted = false
                                }

            str == "." -> { isDotted = true }
            resultField.text != "" -> {
                numberField.text = resultField.text
                resultField.text = ""
            }
        }
        numberField.append(str)
        prevSym = str


    }


}