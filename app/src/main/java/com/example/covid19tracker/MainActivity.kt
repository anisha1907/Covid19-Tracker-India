package com.example.covid19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import android.widget.AbsListView
import java.util.concurrent.TimeUnit

class MainActivity<long> : AppCompatActivity() {

    lateinit var covidAdapter:CovidAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,list,false))
        fetchResults()
    }
    private fun fetchResults(){
        GlobalScope.launch {
        val response=withContext(Dispatchers.IO){Client.api.execute()}
        if(response.isSuccessful){
            val data=Gson().fromJson(response.body?.string(),Response::class.java)
            launch(Dispatchers.Main){
                bindCombinedData(data.statewise[0])
                bindStatewiseData(data.statewise.subList(1,data.statewise.size))
            }
        }
        }
    }

    private fun bindStatewiseData(subList: List<StatewiseItem>) {
        covidAdapter=CovidAdapter(subList)
        list.adapter=covidAdapter

    }

    private fun bindCombinedData(data:StatewiseItem) {
        val lastUpdatedTime=data.lastupdatedtime
        val simpleDateFormat =SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdatedTV.text="LAST UPDATED \n${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"
        confirmedTv.text=data.confirmed
        recoveredTv.text=data.recovered
        activeTv.text=data.active
        deathTv.text=data.deaths

    }
    fun getTimeAgo(past: Date):String{
        val now=Date()
        val seconds: Long =TimeUnit.MILLISECONDS.toSeconds(now.time-past.time)
        val minutes: Long=TimeUnit.MILLISECONDS.toMinutes(now.time-past.time)
        val hours: Long=TimeUnit.MILLISECONDS.toHours(now.time-past.time)
        return when{
            seconds<60->{
                "Few Seconds Ago"
            }
            minutes<60->{
                "$minutes Minutes Ago"
            }
            hours<24->{
                "$hours Hours Ago"
            }
            else->{
                SimpleDateFormat("dd/MM/yy,hh:mm:a").format(past).toString()
            }
        }
    }


}
