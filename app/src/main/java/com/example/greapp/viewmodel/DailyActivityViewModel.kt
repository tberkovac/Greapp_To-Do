package com.example.greapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greapp.models.DailyActivity
import com.example.greapp.repositories.DailyActivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KFunction1

class DailyActivityViewModel : ViewModel() {
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    val todaysActivities: MutableLiveData<List<DailyActivity>> by lazy {
        MutableLiveData<List<DailyActivity>>()
    }

    val currentActivity: MutableLiveData<DailyActivity?> by lazy {
        MutableLiveData()
    }

    fun writeActivity (activity: DailyActivity) {
        scope.launch {
            DailyActivityRepository.writeActivity(activity)
            todaysActivities.value = DailyActivityRepository.getTodaysActivities()
            currentActivity.value = DailyActivityRepository.getCurrentActivity()
        }
    }

    fun getActivities(onSuccess: KFunction1<List<DailyActivity>, Unit>) {
        scope.launch {
            val activites = DailyActivityRepository.getAllActivities()
            onSuccess.invoke(activites)
        }
    }

    fun getTodaysActivities(onSuccess: (List<DailyActivity>)-> Unit){
        scope.launch {
            val activities = DailyActivityRepository.getTodaysActivities()
            todaysActivities.value = activities
            onSuccess.invoke(activities)
        }
    }

    fun getCurrentActivity(onSuccess: (DailyActivity?)->Unit){
        scope.launch {
            val rez = DailyActivityRepository.getCurrentActivity()
            currentActivity.value = rez
            onSuccess.invoke(rez)
        }
    }

    fun activityDoneUpdate(id: Int) {
        scope.launch {
            DailyActivityRepository.activityDoneUpdate(Calendar.getInstance().time,id)
            todaysActivities.value = DailyActivityRepository.getTodaysActivities()
            currentActivity.value = DailyActivityRepository.getCurrentActivity()
        }
    }

    fun updateActivities(time: Long, start: Long, end: Long) {
        scope.launch {
            DailyActivityRepository.updateTimes(time, start, end)
            todaysActivities.value = DailyActivityRepository.getTodaysActivities()
            currentActivity.value = DailyActivityRepository.getCurrentActivity()
        }
    }

    fun deleteActivity(activity: DailyActivity) {
        scope.launch {
            DailyActivityRepository.deleteActivity(activity)
            todaysActivities.value = DailyActivityRepository.getTodaysActivities()
            currentActivity.value = DailyActivityRepository.getCurrentActivity()
        }
    }
}