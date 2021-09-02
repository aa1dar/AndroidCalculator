package com.example.calculator

import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.sqrt


class Calculator(val str:String) {

    fun getAnswer():Number{

        val num = calculate(str)


        if(num.roundToInt()/1.0 == num)
            return num.roundToInt()
        else {
            val df = DecimalFormat("#.#######")
            df.roundingMode = RoundingMode.CEILING
            return df.format(num).replace(',','.').toDouble()
        }
    }

    private fun calculate(str:String): Double {

        val inp:MutableList<TokenType> = tokenize(str)
        //from infix to postfix
        val gPostfixQueue: LinkedList<TokenType> = LinkedList<TokenType>()
        gPostfixQueue.clear()
        val shuntingStack: LinkedList<TokenType> = LinkedList<TokenType>()

        while(inp.size>0){
            val temp = inp.removeFirst()

            if(temp is NUM){ gPostfixQueue.addLast(temp)}
            else if (temp==UNAMINUS || temp==SQRT){
                shuntingStack.addLast(temp)
            }
            else if( temp==PLUS || temp==BINMINUS || temp==DEV || temp==MULT){
                while (shuntingStack.size>0 && shuntingStack.last.precidence >= temp.precidence)
                    gPostfixQueue.addLast(shuntingStack.removeLast())
                shuntingStack.addLast(temp)

            }
            else if (temp == LPAREN){
                shuntingStack.addLast(temp)
            }
            else if( temp == RPAREN){
                try{
                    while(shuntingStack.last != LPAREN)
                        gPostfixQueue.addLast(shuntingStack.removeLast())

                }
                catch (e: NoSuchElementException){
                    throw Exception("Missing parentheses")

                }
                shuntingStack.removeLast()
            }




        }
        while (shuntingStack.size>0)
        {
            if (shuntingStack.last is NUM || shuntingStack.last == LPAREN || shuntingStack.last == RPAREN)
            {
                gPostfixQueue.clear();
                throw Exception("Non-operator on shunting stack")
            }
            else gPostfixQueue.addLast(shuntingStack.removeLast())
        }
        //postfix complete

        if (gPostfixQueue.size==0) return 0.0
        val postfixQueue: LinkedList<TokenType> = LinkedList(gPostfixQueue)
        val rpevalStack = LinkedList<Number>()
        rpevalStack.clear()

        while (postfixQueue.size > 0) {
            try {
                val t = postfixQueue.removeFirst()
                if (t is NUM) rpevalStack.addLast(t.value)
                else if (!(t is NUM || t == LPAREN || t == RPAREN)) {
                    val a1 = rpevalStack.removeLast().toDouble()
                    if (t == PLUS) rpevalStack.addLast((a1 + rpevalStack.removeLast().toDouble()))
                    else if (t == BINMINUS) rpevalStack.addLast(rpevalStack.removeLast().toDouble() - a1)
                    else if (t === UNAMINUS) rpevalStack.addLast(-a1)
                    else if (t == SQRT) rpevalStack.addLast(sqrt(a1))
                    else if (t == MULT) rpevalStack.addLast(a1 * rpevalStack.removeLast().toDouble())
                    else if (t== DEV) rpevalStack.addLast(rpevalStack.removeLast().toDouble() / a1)
                }
            } catch (e: NoSuchElementException) {
                throw Exception("No more tokens to evaluate")

            }

        }
        if (rpevalStack.size!=1)
            throw Exception("Invalid postfix");
        else return rpevalStack.last.toDouble();
    }

}

val integerChars = '0'..'9'
fun isNumber(input: String): Boolean {
    var dotOccurred = 0
    return input.all { it in integerChars || it == '.' && dotOccurred++ < 1 }
}

fun shouldAddMult(tt:TokenType?): Boolean{
    return (tt is NUM || tt == RPAREN)
}

fun tokenize(str:String): MutableList<TokenType>{
    val res:MutableList<TokenType> = mutableListOf()
    var prevTok: TokenType? = null

    val regex = """(?<=[-−+*/()√])|(?=[-−+*/()√])""".toRegex()
    val toks: List<String> = str.split(regex)

    for (x in toks) {
        when{
            x.isBlank() -> continue
            isNumber(x) ->{
                if(shouldAddMult(prevTok))
                    res.add(MULT)
                if(!x.isEmpty()) {
                    res.add(NUM(x.toDouble()))
                    prevTok = NUM(x.toDouble())
                }

            }

            x == "(" -> {
                if(shouldAddMult(prevTok))
                    res.add(MULT)
                res.add(LPAREN)
                prevTok = LPAREN
            }
            x==")" ->{
                res.add(RPAREN)
                prevTok = RPAREN
            }
            x=="+" ->{
                res.add(PLUS)
                prevTok = PLUS
            }
            x=="-" ->{
                if(prevTok!= null && prevTok==SQRT){
                    throw Exception("InvalidOperatorException: Operator:$x \n Expression: $str")
                }
                if(prevTok==null || ((prevTok !is NUM) && prevTok!=RPAREN)) {
                    res.add(UNAMINUS)
                    prevTok = UNAMINUS
                }
                else{
                    res.add(BINMINUS)
                    prevTok = BINMINUS
                }
            }
            x=="/" ->{
                res.add(DEV)
                prevTok = DEV
            }
            x=="*" ->{
                res.add(MULT)
                prevTok = MULT
            }
            x=="√" ->{
                if(shouldAddMult(prevTok))
                    res.add(MULT)
                res.add(SQRT)
                prevTok = SQRT
            }
            else ->
                throw Exception("InvalidOperatorException: Operator:$x \n Expression: $str")
        }
    }

    return res
}
