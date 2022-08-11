package com.example.greapp.repositories

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


class SendWorker(private var appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    val TAG = "SendWorker"
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val cal = Calendar.getInstance() //u ponoc
            cal.set(Calendar.HOUR, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)

            val date = cal.time
22
            try {
                val db = GreappDB.getInstance(appContext)
                val dailySize = db.dailyActivityDao().getAll().size
                val allTimeSize = db.allTimeActivityDao().getAll().size
                Log.v("BEFORESWITCH", "daily: " + dailySize.toString() + " and ALL: " + allTimeSize.toString())
                db.allTimeActivityDao().populateForYesterday(date.time)
                db.dailyActivityDao().deleteYesterdays(date.time)
                val dailySize2 = db.dailyActivityDao().getAll().size
                val allTimeSize2 = db.allTimeActivityDao().getAll().size
                Log.v("AFTERSWITCH", "daily: " + dailySize2.toString() + " and ALL: " + allTimeSize2.toString())
                Log.v(TAG, "poslanooooo")
                return@withContext Result.success()
            } catch (e: Exception){
                return@withContext Result.failure()
            }


        }
    }



}

