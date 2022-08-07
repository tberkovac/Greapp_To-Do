package com.example.greapp.repositories

import android.content.Context
import com.example.greapp.models.DailyActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    }
}