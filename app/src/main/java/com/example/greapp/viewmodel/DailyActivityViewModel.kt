package com.example.greapp.viewmodel

import com.example.greapp.models.DailyActivity
import com.example.greapp.repositories.DailyActivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KFunction1

class DailyActivityViewModel {
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun writeActivity (activity: DailyActivity) {
        scope.launch {
            DailyActivityRepository.writeActivity(activity)
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
            val todayDate = Calendar.getInstance().time
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
            var activities = DailyActivityRepository.getAllActivities()
            activities = activities.filter { it.startTime?.let { it1 -> simpleDateFormat.format(it1).equals(simpleDateFormat.format(todayDate)) } ?:false }
            onSuccess.invoke(activities)
        }
    }

    fun getCurrentActivity(onSuccess: (DailyActivity?)->Unit){
        scope.launch {
            val todayDate = Calendar.getInstance().time
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
            var activities = DailyActivityRepository.getAllActivities()
            activities = activities.filter {
                it.startTime?.let { it1 ->
                    simpleDateFormat.format(it1).equals(simpleDateFormat.format(todayDate))
                } ?: false
            }.filter { it.startTime?.before(todayDate) ?: false && it.expectedEndTime?.after(todayDate) ?: false }

            var rez = if(activities.isEmpty()) null else activities[0]
            onSuccess.invoke(rez)
        }
    }

    fun activityDoneUpdate(id: Int) {
        scope.launch {
            DailyActivityRepository.activityDoneUpdate(Calendar.getInstance().time,id)
        }
    }

    fun updateActivities(time: Long, start: Long, end: Long) {
        scope.launch {
            DailyActivityRepository.updateTimes(time, start, end)
        }
    }

    fun deleteActivity(activity: DailyActivity) {
        scope.launch {
            DailyActivityRepository.deleteActivity(activity)
        }
    }
}