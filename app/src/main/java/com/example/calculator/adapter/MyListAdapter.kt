package com.example.calculator.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


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

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        val (exp, res) = arrayList[position]
        var convertView: View? = convertView
        convertView =
            LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
        expression = convertView.findViewById(android.R.id.text1)
        result = convertView.findViewById(android.R.id.text2)

        expression.text = exp
        result.text = res
        return convertView
    }
}

