package com.example.calculator


import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.adapter.MyListAdapter
import com.example.calculator.db.DbManager


class History : AppCompatActivity() {

    val DbManager = DbManager(this)
    var adapter: MyListAdapter? = null
    lateinit var tvList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        tvList = findViewById(R.id.tvList)

        // присваиваем адаптер списку
        adapter = MyListAdapter(this, getArray())

        tvList.adapter = adapter
    }

    fun getArray(): ArrayList<Pair<String, String>> {
        DbManager.open()
        val array = DbManager.readOfDb()

        DbManager.close()
        return array
    }

}

