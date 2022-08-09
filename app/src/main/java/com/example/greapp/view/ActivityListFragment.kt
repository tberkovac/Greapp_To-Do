package com.example.greapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greapp.R
import com.example.greapp.models.DailyActivity
import com.example.greapp.viewmodel.DailyActivityViewModel

class ActivityListFragment() : Fragment() {
    private lateinit var activityRV : RecyclerView
    private val dailyActivityViewModel = DailyActivityViewModel()
    private lateinit var adapter : ActivityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_list_fragment, container, false)
        activityRV = view.findViewById(R.id.lvActivitiesID)

        adapter = activity?.let { ActivityListAdapter(listOf(), it) }!!
        activityRV.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        activityRV.layoutManager = linearLayoutManager

        dailyActivityViewModel.getTodaysActivities(::onSuccessGetActivities)

        return view
    }

    private fun onSuccessGetActivities(act : List<DailyActivity>){
        adapter.updateActivities(act)
        adapter.notifyDataSetChanged()
    }

}