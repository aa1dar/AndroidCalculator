package com.example.calculator


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        // присваиваем адаптер списку
        tvList.adapter = adapter
    }

    fun getArray(): ArrayList<Pair<String, String>> {
        DbManager.open()
        val array = DbManager.readOfDb()

        DbManager.close()
        return array
    }

}

class MyListAdapter(
    private val context: Context,
    private val arrayList: ArrayList<Pair<String, String>>
) : BaseAdapter() {
    private lateinit var expression: TextView
    private lateinit var result: TextView

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {


        val (exp, res) = arrayList[position]
        var convertView = convertView
        convertView =
            LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
        expression = convertView.findViewById(android.R.id.text1)
        result = convertView.findViewById(android.R.id.text2)

        expression.text = exp
        result.text = res
        return convertView
    }
}