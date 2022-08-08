package com.example.greapp.view

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greapp.R
import com.example.greapp.models.DailyActivity
import com.example.greapp.viewmodel.DailyActivityViewModel
import java.util.*

class ActivityListAdapter(
    private var activities : List<DailyActivity>):
    RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder>() {

    private val dailyActivityViewModel = DailyActivityViewModel()

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var naziv : TextView = itemView.findViewById(R.id.name)
        var startText : TextView = itemView.findViewById(R.id.start)
        var endText : TextView = itemView.findViewById(R.id.end)
        var ivDone : ImageView = itemView.findViewById(R.id.done)
        var ivTrash : ImageView = itemView.findViewById(R.id.trash)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lwacard, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.naziv.text = activities[position].name
        holder.startText.text = formatirajDatum(activities[position].startTime)
        holder.endText.text = formatirajDatum(activities[position].expectedEndTime)

        var currActivity = activities[position]

        holder.ivDone.setOnClickListener {
            if(currActivity.markedFinishedTime == null && currActivity.startTime.before(Calendar.getInstance().time)) {
                val id = activities[position].id
                dailyActivityViewModel.activityDoneUpdate(id)
                dailyActivityViewModel.getActivities(::updateActivities)
            }
        }

        holder.ivTrash.setOnClickListener {
            dailyActivityViewModel.deleteActivity(currActivity)
            dailyActivityViewModel.getActivities(::updateActivities)
        }
        if(currActivity.markedFinishedTime != null) {
            if (currActivity.markedFinishedTime!!.before(currActivity.expectedEndTime)
                && currActivity.markedFinishedTime!!.after(currActivity.startTime)
            ) {
                holder.itemView.setBackgroundColor(Color.parseColor("#cefad0"))
            }else if( currActivity.markedFinishedTime!!.after(currActivity.expectedEndTime)){
                holder.itemView.setBackgroundColor(Color.parseColor("#fffbc8"))
            }
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    fun updateActivities(act : List<DailyActivity>){
        activities = act
        notifyDataSetChanged()
    }

    private fun formatirajDatum(date : Date) : String {
        return SimpleDateFormat("HH:mm").format(date)
    }
}