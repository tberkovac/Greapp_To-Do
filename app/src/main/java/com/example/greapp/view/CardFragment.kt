package com.example.greapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.greapp.R
import com.example.greapp.models.DailyActivity
import com.example.greapp.viewmodel.DailyActivityViewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class CardFragment : Fragment() {
    private lateinit var tvNameOfActivity : TextView
    private lateinit var tvHoursAndMinutes : TextView
    private lateinit var note: TextView


    private var dailyActivityViewModel = DailyActivityViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_card, container, false)
        tvNameOfActivity = view.findViewById(R.id.trenutnaId)
        tvHoursAndMinutes = view.findViewById(R.id.tvSatiIMinuteId)
        note = view.findViewById(R.id.noteHomeTV)

        dailyActivityViewModel.getCurrentActivity(::setNameActivityTV)
        return view
    }

    private fun setNameActivityTV(dailyActivity: DailyActivity?){
        val dtf = SimpleDateFormat("hh:mm")
        if(dailyActivity == null){
            tvNameOfActivity.text = "Enjoy your break"
            tvHoursAndMinutes.text = ":))))"
        }else {
            tvNameOfActivity.text = dailyActivity.name
            tvHoursAndMinutes.text = dtf.format(dailyActivity.expectedEndTime)
            note.text = dailyActivity.note
        }
    }
}