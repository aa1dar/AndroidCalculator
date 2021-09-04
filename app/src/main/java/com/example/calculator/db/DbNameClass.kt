package com.example.calculator.db

import android.provider.BaseColumns

object DbNameClass : BaseColumns {
    const val TABLE_NAME = "calculations"
    const val COLUMN_NAME_EXPRESSION = "expression"
    const val COLUMN_NAME_RESULT = "result"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "Calc.db"

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE IF NOT EXISTS ${TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${COLUMN_NAME_EXPRESSION} TEXT," +
                "${COLUMN_NAME_RESULT} TEXT)"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"

}


