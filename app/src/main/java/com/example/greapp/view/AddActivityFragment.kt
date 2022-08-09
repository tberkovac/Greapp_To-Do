package com.example.greapp.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.greapp.MainActivity
import com.example.greapp.R
import com.example.greapp.models.DailyActivity
import com.example.greapp.models.TimeHM
import com.example.greapp.viewmodel.DailyActivityViewModel
import java.util.*

class AddActivityFragment : Fragment() {
    private lateinit var spinnerCategory: Spinner
    private lateinit var switch: Switch
    private lateinit var note : EditText
    private lateinit var btnStartTime : ImageView
    private lateinit var btnEndTime : ImageView
    private lateinit var tvStartTime : TextView
    private lateinit var tvEndTime : TextView
    private lateinit var startTime : TimeHM
    private lateinit var endTime : TimeHM
    private lateinit var finish : Button
    private var timeActivity = false
    private var dailyActivityViewModel = DailyActivityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_activity_fragment, container, false)
        spinnerCategory = view.findViewById(R.id.spinner)
        note = view.findViewById(R.id.eTNote)
        switch = view.findViewById(R.id.switchConsecutiveActivity)
        btnStartTime = view.findViewById(R.id.pickStartTimeId)
        btnEndTime = view.findViewById(R.id.pickEndTimeId)
        tvStartTime = view.findViewById(R.id.tvStartNumbersId)
        tvEndTime = view.findViewById(R.id.tvEndTimeNumbersId)
        finish = view.findViewById(R.id.btnFinish)

        btnStartTime.isEnabled = false
        btnEndTime.isEnabled = false

        val podaci = listOf("Studying", "Eating", "Journaling", "Exercise", "Religious activity", "Friends & Family", "Self-care")
        var spinnerAdapter = ArrayAdapter<String> (
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            podaci)
        spinnerCategory.adapter = spinnerAdapter

        switch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                timeActivity = true
                btnStartTime.isEnabled = true
                btnEndTime.isEnabled = true
            }else{
                btnStartTime.isEnabled = false
                btnEndTime.isEnabled = false
                timeActivity = false
            }
        }

        btnStartTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            TimePickerDialog(
                activity,
                { timePicker, i, i2 -> postaviITextView(i, i2) },
                hour,
                minute,
                DateFormat.is24HourFormat(activity)
            ).show()
        }

        btnEndTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            TimePickerDialog(
                activity,
                { timePicker, i, i2 -> postaviITextViewEnd(i, i2) },
                hour,
                minute,
                DateFormat.is24HourFormat(activity)
            ).show()
        }

        finish.setOnClickListener {
            if(timeActivity){
                var cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, startTime.hours)
                cal.set(Calendar.MINUTE, startTime.minutes)
                val startDate : Date = cal.time

                val cal2 = Calendar.getInstance()
                cal2.set(Calendar.HOUR_OF_DAY, endTime.hours)
                cal2.set(Calendar.MINUTE, endTime.minutes)
                val endDate : Date = cal2.time

                val newActivity = DailyActivity(spinnerCategory.selectedItem.toString(), note.text.toString(), startDate,endDate, null)
                dailyActivityViewModel.writeActivity(newActivity)
            }else{
                val newActivity = DailyActivity(spinnerCategory.selectedItem.toString(), note.text.toString(), null, null, null)
                dailyActivityViewModel.writeActivity(newActivity)
            }

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