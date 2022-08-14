package com.example.greapp.repositories

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.greapp.models.AllTimeActivity
import com.example.greapp.models.DailyActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


class SendWorker(private var appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val midnight = setDateToMidnight()
            var cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1)
            try {
                val db = GreappDB.getInstance(appContext)
                var pastActivities = db.dailyActivityDao().getPast(midnight.time)
                val allTimePastMapped = pastActivities.map { AllTimeActivity(it.name,it.note,it.startTime,it.expectedEndTime,it.markedFinishedTime) }
                db.allTimeActivityDao().insertAll(allTimePastMapped)
                db.dailyActivityDao().deletePast(midnight.time)
                db.dailyActivityDao().insert(DailyActivity("SA WORKERA", "", Calendar.getInstance().time,cal.time,null))

                db.allTimeActivityDao().insertAll(allTimePastMapped)
                db.dailyActivityDao().deletePast(midnight.time)
                Log.v("SendWorker", "poslanooooo")
                return@withContext Result.success()
            } catch (e: Exception){
                return@withContext Result.failure()
            }
        }
    }

    private fun setDateToMidnight() : Date{
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        return cal.time
    }
}

