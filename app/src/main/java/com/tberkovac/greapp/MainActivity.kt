package com.tberkovac.greapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import androidx.work.*
import com.tberkovac.greapp.repositories.AllTimeActivityRepository
import com.tberkovac.greapp.repositories.DailyActivityRepository
import com.tberkovac.greapp.repositories.SendWorker
import com.tberkovac.greapp.view.ViewPagerAdapter
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


        val workReq = PeriodicWorkRequestBuilder<SendWorker>(15,
            TimeUnit.MINUTES, 5, TimeUnit.MINUTES).addTag("SendWorker").build()

       // WorkManager.getInstance(application).cancelAllWork()
        WorkManager.getInstance(application).enqueueUniquePeriodicWork("SendWorker", ExistingPeriodicWorkPolicy.KEEP,workReq)

    }
}