package com.example.greapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.greapp.repositories.SendWorker
import java.util.concurrent.TimeUnit

class SendToAllTimeViewModel(application: Application) : AndroidViewModel(application) {
    private val workManager : WorkManager = WorkManager.getInstance(application)

    internal fun send(){
        val sendRequest = PeriodicWorkRequestBuilder<SendWorker>(24,TimeUnit.HOURS).build()
        workManager.enqueue(sendRequest)
    }
}