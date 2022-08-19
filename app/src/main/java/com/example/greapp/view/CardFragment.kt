package com.example.greapp.view

import android.content.BroadcastReceiver
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.greapp.MainActivity
import com.example.greapp.R
import com.example.greapp.models.DailyActivity
import com.example.greapp.viewmodel.DailyActivityViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class CardFragment : Fragment() {
    private lateinit var tvNameOfActivity : TextView
    private lateinit var tvHoursAndMinutes : TextView
    private lateinit var note: TextView
    private lateinit var activityUpdateReciever: BroadcastReceiver

    private val dailyActivityViewModel : DailyActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_card, container, false)
        tvNameOfActivity = view.findViewById(R.id.trenutnaId)
        tvHoursAndMinutes = view.findViewById(R.id.tvSatiIMinuteId)
        note = view.findViewById(R.id.noteHomeTV)

        dailyActivityViewModel.getCurrentActivity(::updateCurrentActivity)

        val currentActivityObserver = Observer<DailyActivity?> { currActivity ->
            setNameActivityTV(currActivity)
        }
        dailyActivityViewModel.currentActivity.observe(viewLifecycleOwner, currentActivityObserver)
        Log.v("haha", MainActivity.viewPagerAdapter.createFragment(0).toString())

        return view
    }

    private fun updateCurrentActivity(currentActivity: DailyActivity?){
        dailyActivityViewModel.currentActivity.value = currentActivity
    }

    private fun setNameActivityTV(dailyActivity: DailyActivity?){
        val dtf = SimpleDateFormat("HH:mm")
        if(dailyActivity == null){
            tvNameOfActivity.text = "Enjoy your break"
            tvHoursAndMinutes.text = ":))))"
        }else {
            tvNameOfActivity.text = dailyActivity.name
            tvHoursAndMinutes.text = "until  " + dtf.format(dailyActivity.expectedEndTime)
            note.text = dailyActivity.note

            CoroutineScope(Dispatchers.IO).launch {
                GlobalScope.launch {
                    delay(dailyActivity.expectedEndTime!!.time - Calendar.getInstance().time.time)
                }.join()
                if(MainActivity.viewPagerAdapter.createFragment(0) is HomeFragment){
                    dailyActivityViewModel.getCurrentActivity(::updateCurrentActivity)
                }
            }
        }
    }
}