package com.tberkovac.greapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tberkovac.greapp.R
import com.tberkovac.greapp.viewmodel.DailyActivityViewModel
import androidx.lifecycle.Observer;
import com.tberkovac.greapp.models.DailyActivity

class ActivityListFragment : Fragment() {
    private lateinit var activityRV : RecyclerView
    private val dailyActivityViewModel : DailyActivityViewModel by activityViewModels()
    private lateinit var adapter : ActivityListAdapter
    private lateinit var btnVremenski : Button
    private lateinit var btnBezvremenski : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_list_fragment, container, false)
        activityRV = view.findViewById(R.id.lvActivitiesID)
        btnBezvremenski = view.findViewById(R.id.btnBezvremenski)
        btnVremenski = view.findViewById(R.id.btnVremenski)
        adapter = activity?.let { ActivityListAdapter(dailyActivityViewModel, it) }!!
        activityRV.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        activityRV.layoutManager = linearLayoutManager

        dailyActivityViewModel.getTodaysActivities()

        val todaysActivitiesObserver = Observer<List<DailyActivity>> { activities ->
            adapter.updateActivities(activities)
        }

        btnVremenski.setOnClickListener {
            btnVremenski.visibility = View.INVISIBLE
            btnBezvremenski.visibility = View.VISIBLE
            dailyActivityViewModel.getTodaysActivities()
        }

        btnBezvremenski.setOnClickListener {
            btnVremenski.visibility = View.VISIBLE
            btnBezvremenski.visibility = View.INVISIBLE
            dailyActivityViewModel.getNoTimeActivities()
        }

        dailyActivityViewModel.todaysActivities.observe(viewLifecycleOwner, todaysActivitiesObserver)
        dailyActivityViewModel.noTimeActivites.observe(viewLifecycleOwner, todaysActivitiesObserver)

        return view
    }

    private fun onSuccessGetActivities(act : List<DailyActivity>){
        dailyActivityViewModel.todaysActivities.value = act
    }

}