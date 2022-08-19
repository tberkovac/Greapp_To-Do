package com.tberkovac.greapp.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tberkovac.greapp.models.AllTimeActivity
import com.tberkovac.greapp.models.DailyActivity

@Database (entities = arrayOf(DailyActivity::class, AllTimeActivity::class), version = 3)
@TypeConverters(DateConverter::class)
abstract class GreappDB : RoomDatabase() {
    abstract fun dailyActivityDao() : DailyActivityDao
    abstract fun allTimeActivityDao() : AllTimeActivityDao

    companion object {
        private var INSTANCE: GreappDB? = null
        fun getInstance(context: Context): GreappDB {
            if (INSTANCE == null) {
                synchronized(GreappDB::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }
        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GreappDB::class.java,
                "GreappDB"
            ).enableMultiInstanceInvalidation().fallbackToDestructiveMigration().build()
    }
}