package com.example.greapp.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.greapp.MainActivity
import com.example.greapp.R
import com.example.greapp.models.DailyActivity
import com.example.greapp.models.TimeHM
import com.example.greapp.viewmodel.DailyActivityViewModel
import java.util.*

class AddActivityFragment : Fragment() {
    private lateinit var btnStartTime : Button
    private lateinit var btnEndTime : Button
    private lateinit var tvStartTime : TextView
    private lateinit var tvEndTime : TextView
    private lateinit var startTime : TimeHM
    private lateinit var endTime : TimeHM
    private lateinit var finish : Button
    private var dailyActivityViewModel = DailyActivityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_activity_fragment, container, false)
        btnStartTime = view.findViewById(R.id.pickStartTimeId)
        btnEndTime = view.findViewById(R.id.pickEndTimeId)
        tvStartTime = view.findViewById(R.id.tvStartNumbersId)
        tvEndTime = view.findViewById(R.id.tvEndTimeNumbersId)
        finish = view.findViewById(R.id.btnFinish)

        btnStartTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            TimePickerDialog(activity, { timePicker, i, i2 -> postaviITextView(i, i2) }, hour, minute, DateFormat.is24HourFormat(activity))
                .show()
        }

        btnEndTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            TimePickerDialog(activity, { timePicker, i, i2 -> postaviITextViewEnd(i, i2)}, hour, minute, DateFormat.is24HourFormat(activity))
                .show()
        }

        finish.setOnClickListener {
            var cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, startTime.hours)
            cal.set(Calendar.MINUTE, startTime.minutes)
            val startDate : Date = cal.time

            val cal2 = Calendar.getInstance()
            cal2.set(Calendar.HOUR_OF_DAY, endTime.hours)
            cal2.set(Calendar.MINUTE, endTime.minutes)
            val endDate : Date = cal2.time

            val newActivity = DailyActivity("nova aktivnost", "bla bla", startDate,endDate, null)
            dailyActivityViewModel.writeActivity(newActivity)

            MainActivity.viewPagerAdapter.addMultiple(listOf(HomeFragment(), ActivityListFragment()))
            MainActivity.viewPagerAdapter.notifyDataSetChanged()
        }

        return view
    }

    fun postaviITextView (hours : Int, minutes:Int) {
        startTime = TimeHM(hours,minutes)
        tvStartTime.text = "$hours : $minutes"
    }

    fun postaviITextViewEnd (hours : Int, minutes:Int) {
        endTime = TimeHM(hours,minutes)
        tvEndTime.text = "$hours : $minutes"
    }




}