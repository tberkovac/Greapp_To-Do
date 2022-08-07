package com.example.greapp.view

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greapp.R
import com.example.greapp.models.DailyActivity
import java.util.*

class ActivityListAdapter(
    private var activities : List<DailyActivity>):
    RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var naziv : TextView = itemView.findViewById(R.id.name)
        var startText : TextView = itemView.findViewById(R.id.start)
        var endText : TextView = itemView.findViewById(R.id.end)
        var btnDone : TextView = itemView.findViewById(R.id.done)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lwacard, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.naziv.text = activities[position].name
        holder.startText.text = formatirajDatum(activities[position].startTime!!)
        holder.endText.text = formatirajDatum(activities[position].expectedEndTime!!)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    fun updateActivites(act : List<DailyActivity>){
        activities = act
        notifyDataSetChanged()
    }

    private fun formatirajDatum(date : Date) : String {
        return SimpleDateFormat("HH:mm").format(date)
    }
}