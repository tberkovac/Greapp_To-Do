package com.example.greapp.repositories

import android.content.Context
import android.util.Log
import com.example.greapp.models.DailyActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DailyActivityRepository {
    companion object {

        private lateinit var mcontext : Context

        fun setContext (context: Context) {
            mcontext = context
        }
        suspend fun writeActivity(activity: DailyActivity): String? {
            return withContext(Dispatchers.IO) {
                try {
                    val db = GreappDB.getInstance(mcontext)
                    db.dailyActivityDao().insert(activity)
                    return@withContext "success"
                } catch (error: Exception) {
                    error.printStackTrace()
                    return@withContext null
                }
            }
        }

        suspend fun getAllActivities() : List<DailyActivity> {
            return withContext(Dispatchers.IO) {
                val db = GreappDB.getInstance(mcontext)
                return@withContext db.dailyActivityDao().getAll()
            }
        }



        suspend fun getYesterdays() : List<DailyActivity> {
            return withContext(Dispatchers.IO) {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                Log.v("JUCER navecer", cal.time.time.toString())
                Log.v("JUCER ujutro", (cal.time.time-86400000).toString())

                val date = cal.time
                val db = GreappDB.getInstance(mcontext)

                db.allTimeActivityDao().populateForYesterday(date.time)
                return@withContext db.dailyActivityDao().getYesterdays(date.time)
            }
        }

        suspend fun getTodaysActivities() : List<DailyActivity> {
            val todayDate = Calendar.getInstance().time
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
            var activities = getAllActivities()
            activities = activities.filter { it.startTime?.let { it1 -> simpleDateFormat.format(it1).equals(simpleDateFormat.format(todayDate)) } ?:false }
            return activities
        }

        suspend fun getCurrentActivity() : DailyActivity? {
            val todayDate = Calendar.getInstance().time
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
            var activities = DailyActivityRepository.getAllActivities()
            activities = activities.filter {
                it.startTime?.let { it1 ->
                    simpleDateFormat.format(it1).equals(simpleDateFormat.format(todayDate))
                } ?: false
            }.filter { it.startTime?.before(todayDate) ?: false && it.expectedEndTime?.after(todayDate) ?: false && it.markedFinishedTime == null}

            return if(activities.isEmpty()) null else activities[0]
        }
        suspend fun activityDoneUpdate(date : Date, id: Int) {
            return withContext(Dispatchers.IO){
                val db = GreappDB.getInstance(mcontext)
                return@withContext db.dailyActivityDao().update(id, date)
            }
        }

        suspend fun updateTimes(time : Long, start: Long, end: Long){
            return withContext(Dispatchers.IO){
                val db = GreappDB.getInstance(mcontext)
                return@withContext db.dailyActivityDao().update(time, start, end)
            }
        }

        suspend fun deleteActivity(activity: DailyActivity) {
            return withContext(Dispatchers.IO){
                val db = GreappDB.getInstance(mcontext)
                return@withContext db.dailyActivityDao().delete(activity)
            }
        }
    }
}