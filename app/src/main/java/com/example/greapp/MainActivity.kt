package com.example.greapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager2.widget.ViewPager2
import androidx.work.*
import com.example.greapp.repositories.DailyActivityRepository
import com.example.greapp.view.ViewPagerAdapter
import com.example.greapp.repositories.AllTimeActivityRepository
import com.example.greapp.repositories.SendWorker
import com.example.greapp.viewmodel.SendToAllTimeViewModel
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var viewPager2: ViewPager2

    companion object {
        lateinit var viewPagerAdapter : ViewPagerAdapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DailyActivityRepository.setContext(this)
        AllTimeActivityRepository.setContext(this)
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager2 = findViewById(R.id.viewPagerId)

        viewPager2.adapter = viewPagerAdapter


        val workReq = PeriodicWorkRequestBuilder<SendWorker>(24,
            TimeUnit.HOURS, 1, TimeUnit.HOURS).build()

        WorkManager.getInstance(application).enqueueUniquePeriodicWork("SendWorker", ExistingPeriodicWorkPolicy.REPLACE,workReq)


    }
}