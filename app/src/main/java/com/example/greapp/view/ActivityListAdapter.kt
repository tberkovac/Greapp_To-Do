package com.example.greapp.view

import android.app.Activity
import android.app.TimePickerDialog
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.text.Layout
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greapp.R
import com.example.greapp.models.DailyActivity
import com.example.greapp.viewmodel.DailyActivityViewModel
import java.util.*

class ActivityListAdapter(private val dailyActivityViewModel: DailyActivityViewModel, private var mActivity: Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var activities : List<DailyActivity> = listOf()
    private val TIME_ACTIVITY = 0
    private val NO_TIME_ACTIVITY = 1

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var naziv : TextView = itemView.findViewById(R.id.name)
        var startText : TextView = itemView.findViewById(R.id.start)
        var endText : TextView = itemView.findViewById(R.id.end)
        var ivDone : ImageView = itemView.findViewById(R.id.done2)
        var ivTrash : ImageView = itemView.findViewById(R.id.trash2)
        var note : TextView = itemView.findViewById(R.id.note2)
    }

    override fun getItemViewType(position: Int): Int {
        val act : DailyActivity = activities[position]
        if(act.startTime == null) return 1
        return 0
    }

    inner class NoTimeActivityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var naziv2 : TextView = itemView.findViewById(R.id.name3)
        var ivDone2 : ImageView = itemView.findViewById(R.id.done3)
        var ivTrash2 : ImageView = itemView.findViewById(R.id.trash3)
        var note3 : TextView = itemView.findViewById(R.id.note3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View
        if(viewType == TIME_ACTIVITY){
            view = LayoutInflater.from(parent.context).inflate(R.layout.lwacard, parent, false)
            return ActivityViewHolder(view)
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.rwno_time_activity_card, parent, false)
            return NoTimeActivityViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val activity = activities[position]

        if(activity.startTime == null){
            (holder as NoTimeActivityViewHolder).naziv2.text = activity.name
            holder.note3.text = activity.note
            holder.ivDone2.setOnClickListener {
                dailyActivityViewModel.activityNoTimeDoneUpdate(activity.id)
            }
            holder.ivTrash2.setOnClickListener {
                dailyActivityViewModel.deleteNoTimeActivity(activity)
            }

            if(activity.markedFinishedTime != null) {
                holder.itemView.setBackgroundColor(Color.parseColor("#cefad0"))
            }
        }else{
            (holder as ActivityViewHolder).naziv.text = activities[position].name
            holder.startText.text = activities[position].startTime?.let { formatirajDatum(it) }
            holder.endText.text = activities[position].expectedEndTime?.let { formatirajDatum(it) }

            var selectedActivity = activities[position]

            holder.ivDone.setOnClickListener {
                if(selectedActivity.markedFinishedTime == null && selectedActivity.startTime?.before(Calendar.getInstance().time) == true) {
                    val id = activities[position].id
                    dailyActivityViewModel.activityDoneUpdate(id)
                    dailyActivityViewModel.getTodaysActivities()
                }
            }

            holder.note.text = activities[position].note

            holder.ivTrash.setOnClickListener {
                dailyActivityViewModel.deleteActivity(selectedActivity)
                notifyDataSetChanged()
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
                ).show()
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
        dailyActivityViewModel.getTodaysActivities()
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