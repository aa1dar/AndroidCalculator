package com.example.calculator.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class DbManager(val context: Context) {
    val DbHelper = DbHelper(context)

    var db: SQLiteDatabase? = null

    fun open() {
        db = DbHelper.writableDatabase
        deleteSomeRows(20)

    }

    fun insertToDb(expression: String, result: String) {

        val values = ContentValues().apply {
            put(DbNameClass.COLUMN_NAME_EXPRESSION, expression)
            put(DbNameClass.COLUMN_NAME_RESULT, result)
        }

        db?.insert(DbNameClass.TABLE_NAME, null, values)
    }

    fun readOfDb(): ArrayList<Pair<String, String>> {
        val dataList = ArrayList<Pair<String, String>>()
        val cursor = db?.query(
            DbNameClass.TABLE_NAME, null, null, null,
            null, null, "_id DESC"
        )//"_id DESC"

        var i = 0
        with(cursor) {
            //reading 20 elements from db
            while (this?.moveToNext()!! && i < 20) {
                val expressionId =
                    this.getString(getColumnIndexOrThrow(DbNameClass.COLUMN_NAME_EXPRESSION))
                val resultId = this.getString(getColumnIndexOrThrow(DbNameClass.COLUMN_NAME_RESULT))
                dataList.add(Pair(expressionId.toString(), resultId.toString()))
                i++
            }
        }

        cursor?.close()
        return dataList
    }

    fun close() {
        DbHelper.close()

    }

    //Delete old history of calculations (when there's over $count expressions)
    fun deleteSomeRows(count: Int) {
        val array = arrayOf(count.toString())
        db?.execSQL(
            "DELETE FROM ${DbNameClass.TABLE_NAME} WHERE _id NOT IN (" +
                    "SELECT _id FROM ${DbNameClass.TABLE_NAME} ORDER BY _id DESC LIMIT $count);"
        )
    }

}