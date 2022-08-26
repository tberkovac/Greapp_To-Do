package com.tberkovac.greapp.repositories

import android.app.*
import android.content.Context
import android.graphics.Color
import android.icu.util.Calendar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tberkovac.greapp.R
import com.tberkovac.greapp.models.AllTimeActivity
import com.tberkovac.greapp.models.DailyActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*


class SendWorker(
    private var appContext: Context,
    workerParams: WorkerParameters
): CoroutineWorker(appContext, workerParams){

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val midnight = setDateToMidnight()
            val cal = Calendar.getInstance()

            val notification = createNotification()
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(123, notification)
            }

            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1)
            try {
                val db = GreappDB.getInstance(appContext)
                val pastActivities = db.dailyActivityDao().getPast(midnight.time)
                val allTimePastMapped = pastActivities.map { AllTimeActivity(it.name,it.note,it.startTime,it.expectedEndTime,it.markedFinishedTime) }
                db.allTimeActivityDao().insertAll(allTimePastMapped)
                db.dailyActivityDao().deletePast(midnight.time)
                db.dailyActivityDao().insert(DailyActivity("BONUS", "", Calendar.getInstance().time,cal.time,null))

                db.allTimeActivityDao().insertAll(allTimePastMapped)
                db.dailyActivityDao().deletePast(midnight.time)
                return@withContext Result.success()
            } catch (e: Exception){
                return@withContext Result.failure()
            }
        }
    }

    private fun createNotification(): Notification {
        val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "to_do_channel_id",
            "Greapp Notification Channel",
            NotificationManager.IMPORTANCE_HIGH
        ).let {
            it.description = "Greapp Notification Channel"
            it.enableLights(true)
            it.lightColor = Color.RED
            it.enableVibration(true)
            it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200,
                400)
            it
        }

        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(appContext, "to_do_channel_id").apply{
            setSmallIcon(R.drawable.ic_baseline_backup_24)
            setContentTitle("Enjoy your day!")
            setContentText("Bonus activity added and past activities archived")
            setOngoing(false)
            build()
        }

        return notification.build()
    }

    private fun setDateToMidnight() : Date{
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        return cal.time
    }
}

