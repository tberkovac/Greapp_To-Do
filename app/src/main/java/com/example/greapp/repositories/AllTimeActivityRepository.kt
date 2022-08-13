package com.example.greapp.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class AllTimeActivityRepository {
    companion object {
        private lateinit var mcontext: Context

        fun setContext(context: Context) {
            mcontext = context
        }


        suspend fun addAllDaily(): String? {
            return withContext(Dispatchers.IO) {
                try {
                    val db = GreappDB.getInstance(mcontext)
                    db.allTimeActivityDao().populateForYesterday(Calendar.getInstance().time.time)
                    return@withContext "success"
                } catch (error: Exception) {
                    error.printStackTrace()
                    return@withContext null
                }
            }
        }
    }

}
