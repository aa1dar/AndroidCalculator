package com.example.calculator
import kotlin.Number as Double

sealed class TokenType {
    abstract val precidence: Int
}

object PLUS: TokenType() {
    override val precidence: Int = 50
}

object BINMINUS : TokenType() {
    override val precidence: Int = 50
}

object UNAMINUS : TokenType() {
    override val precidence: Int = 100
}
object MULT : TokenType() {
    override val precidence: Int = 60
}
object DEV : TokenType() {
    override val precidence: Int = 60
}
object SQRT : TokenType() {
    override val precidence: Int = 100
}
object RPAREN : TokenType() {
    override val precidence: Int = 0
}
object LPAREN : TokenType() {
    override val precidence: Int = 0
}
data class NUM(val value: Double) :TokenType() {
    override val precidence: Int = 0
}
