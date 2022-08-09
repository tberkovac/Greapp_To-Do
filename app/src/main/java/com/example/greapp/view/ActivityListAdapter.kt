package com.example.greapp.view

import android.app.Activity
import android.app.TimePickerDialog
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
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
    private var activities : List<DailyActivity>,
    private var mActivity: Activity):
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
        holder.startText.text = activities[position].startTime?.let { formatirajDatum(it) }
        holder.endText.text = activities[position].expectedEndTime?.let { formatirajDatum(it) }

        var currActivity = activities[position]

        holder.ivDone.setOnClickListener {
            if(currActivity.markedFinishedTime == null && currActivity.startTime?.before(Calendar.getInstance().time) == true) {
                val id = activities[position].id
                dailyActivityViewModel.activityDoneUpdate(id)
                dailyActivityViewModel.getTodaysActivities(::updateActivities)
            }
        }

        holder.ivTrash.setOnClickListener {
            dailyActivityViewModel.deleteActivity(currActivity)
            dailyActivityViewModel.getTodaysActivities(::updateActivities)
        }

        holder.endText.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            TimePickerDialog(
                mActivity,
                { timePicker, i, i2 -> obracunajIPostaviNovaVremena(currActivity, i, i2) },
                hour,
                minute,
                DateFormat.is24HourFormat(mActivity)
            )
                .show()
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

    private fun obracunajIPostaviNovaVremena(currActivity: DailyActivity, hours: Int, minutes: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hours)
        cal.set(Calendar.MINUTE, minutes)
        cal.set(Calendar.SECOND, 0)

        val date = cal.time

        var diff = date.time - (currActivity.expectedEndTime?.time ?: 0) //in ms

        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)

        dailyActivityViewModel.updateActivities(diff, currActivity.startTime!!.time, cal.timeInMillis)
        dailyActivityViewModel.getTodaysActivities(::updateActivities)
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