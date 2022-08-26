package com.tberkovac.greapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.work.*
import com.tberkovac.greapp.MainActivity
import com.tberkovac.greapp.R
import com.tberkovac.greapp.repositories.SendWorker
import com.tberkovac.greapp.viewmodel.DailyActivityViewModel

class HomeFragment : Fragment() {
    private lateinit var frameLayout : FrameLayout
    private lateinit var addActivityButton: Button
    private lateinit var breakBtn : Button
    private val dailyActivityViewModel : DailyActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        frameLayout = view.findViewById(R.id.frame1Id)
        addActivityButton = view.findViewById(R.id.addActivity)
        breakBtn = view.findViewById(R.id.breakInsert)
        loadFragment(R.id.frame1Id, CardFragment())
        dailyActivityViewModel.getTodaysActivities()
        addActivityButton.setOnClickListener{
            MainActivity.viewPagerAdapter.removeAllAndAdd(AddActivityFragment())
            MainActivity.viewPagerAdapter.notifyDataSetChanged()
        }

        breakBtn.setOnClickListener {
            val worker = OneTimeWorkRequestBuilder<SendWorker>().build()

            WorkManager.getInstance(requireContext()).enqueue(worker)
        }

        return view
    }

    private fun loadFragment (containerId : Int, fragment: Fragment) {
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.add(containerId, fragment)
        transaction.commit()
    }

    private fun replaceFragment (containerId : Int, fragment: Fragment) {
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.replace(containerId, fragment)
        transaction.commit()
    }

}