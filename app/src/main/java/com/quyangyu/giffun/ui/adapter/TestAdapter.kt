package com.quyangyu.giffun.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quyangyu.giffun.R
import kotlinx.android.synthetic.main.adapter_test_item.view.*

class TestAdapter(var context: Context,var data: List<String>) : RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_test_item, parent, false))
    }

    override fun getItemCount(): Int {
        return  data!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          holder.bind(data.get(position))
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
             fun bind(data:String){
                 itemView.tv_show.text=data
             }
    }
}