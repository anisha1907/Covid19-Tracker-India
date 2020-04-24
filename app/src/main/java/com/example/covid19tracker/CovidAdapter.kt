package com.example.covid19tracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_list.view.*

class CovidAdapter(val list:List<StatewiseItem>): BaseAdapter() {
    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view=p1?:LayoutInflater.from(p2?.context)
            .inflate(R.layout.item_list,p2,false)
        val item=list[position]
        view.confirmedTv.text=SpannableDelta("${item.confirmed}\n ↑ ${item.deltaconfirmed?:"0"}","#8B0000",item.confirmed?.length?:0)
        view.activeTv.text=SpannableDelta("${item.active}\n ↑ ${item.deltaactive?:"0"}","#1976D2",item.confirmed?.length?:0)
        view.recoveredTv.text=SpannableDelta("${item.recovered}\n ↑ ${item.deltarecovered?:"0"}","#388E3C",item.confirmed?.length?:0)
        view.deathTv.text=SpannableDelta("${item.deaths}\n ↑ ${item.deltadeaths?:"0"}","#FBC02D",item.confirmed?.length?:0)
        view.stateTv.text=item.state

        return view

    }

    override fun getItem(position: Int) = list[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount(): Int = list.size


}