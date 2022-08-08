package com.example.greapp.viewmodel

import com.example.greapp.models.DailyActivity
import com.example.greapp.repositories.DailyActivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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

    fun activityDoneUpdate(id: Int) {
        scope.launch {
            DailyActivityRepository.activityDoneUpdate(Calendar.getInstance().time,id)
        }
    }

    fun deleteActivity(activity: DailyActivity) {
        scope.launch {
            DailyActivityRepository.deleteActivity(activity)
        }
    }
}