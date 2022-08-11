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

class ActivityListAdapter(private val dailyActivityViewModel: DailyActivityViewModel, private var mActivity: Activity) :
    RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder>() {

    private var activities : List<DailyActivity> = listOf()

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

        var selectedActivity = activities[position]

        holder.ivDone.setOnClickListener {
            if(selectedActivity.markedFinishedTime == null && selectedActivity.startTime?.before(Calendar.getInstance().time) == true) {
                val id = activities[position].id
                dailyActivityViewModel.activityDoneUpdate(id)
                dailyActivityViewModel.getTodaysActivities(::updateActivities)
            }
        }

        holder.ivTrash.setOnClickListener {
            dailyActivityViewModel.deleteActivity(selectedActivity)
            dailyActivityViewModel.getTodaysActivities(::updateActivities)
        }

        holder.endText.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            TimePickerDialog(
                mActivity,
                { timePicker, i, i2 -> obracunajIPostaviNovaVremena(selectedActivity, i, i2) },
                hour,
                minute,
                DateFormat.is24HourFormat(mActivity)
            )
                .show()
        }

        if(selectedActivity.markedFinishedTime != null) {
            if (selectedActivity.markedFinishedTime!!.before(selectedActivity.expectedEndTime)
                && selectedActivity.markedFinishedTime!!.after(selectedActivity.startTime)
            ) {
                holder.itemView.setBackgroundColor(Color.parseColor("#cefad0"))
            }else if( selectedActivity.markedFinishedTime!!.after(selectedActivity.expectedEndTime)){
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

    fun updateActivities(updatedActivities : List<DailyActivity>){
        activities = updatedActivities
        activities = activities.sortedBy { it.startTime }
        notifyDataSetChanged()
    }

    private fun formatirajDatum(date : Date) : String {
        return SimpleDateFormat("HH:mm").format(date)
    }

    override fun getItemCount(): Int {
        return activities.size
    }
}