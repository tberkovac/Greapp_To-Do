package com.example.greapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greapp.R
import com.example.greapp.viewmodel.DailyActivityViewModel
import androidx.lifecycle.Observer;
import com.example.greapp.models.DailyActivity

class ActivityListFragment() : Fragment() {
    private lateinit var activityRV : RecyclerView
    private val dailyActivityViewModel : DailyActivityViewModel by viewModels()
    private lateinit var adapter : ActivityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_list_fragment, container, false)
        activityRV = view.findViewById(R.id.lvActivitiesID)

        adapter = activity?.let { ActivityListAdapter( it) }!!
        activityRV.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        activityRV.layoutManager = linearLayoutManager

        dailyActivityViewModel.getTodaysActivities(::onSuccessGetActivities)

        val todaysActivitiesObserver = Observer<List<DailyActivity>> { activities ->
            adapter.updateActivities(activities)
        }
        val currentActivityObserver = Observer<DailyActivity?> { currActivity ->

        }
        dailyActivityViewModel.currentActivity.observe(viewLifecycleOwner, currentActivityObserver)
        dailyActivityViewModel.todaysActivities.observe(viewLifecycleOwner, todaysActivitiesObserver)

        return view
    }

    private fun onSuccessGetActivities(act : List<DailyActivity>){
        dailyActivityViewModel.todaysActivities.value = act
    }

}