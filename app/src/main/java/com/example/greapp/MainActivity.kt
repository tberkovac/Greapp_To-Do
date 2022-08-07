package com.example.greapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.greapp.repositories.DailyActivityRepository
import com.example.greapp.view.ViewPagerAdapter

class MainActivity : AppCompatActivity() {
    lateinit var viewPager2: ViewPager2
    companion object {
        lateinit var viewPagerAdapter : ViewPagerAdapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DailyActivityRepository.setContext(this)
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager2 = findViewById(R.id.viewPagerId)

        viewPager2.adapter = viewPagerAdapter
    }
}