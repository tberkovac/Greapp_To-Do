package com.example.greapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.greapp.R

class HomeFragment : Fragment() {
    private lateinit var frameLayout : FrameLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        frameLayout = view.findViewById(R.id.frame1Id)
        loadFragment(R.id.frame1Id, CardFragment())
        loadFragment(R.id.frame2Id, CardFragment2())
        return view
    }

    private fun loadFragment (containerId : Int, fragment: Fragment) {
        val transaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.add(containerId, fragment)
        transaction.commit()
    }
}