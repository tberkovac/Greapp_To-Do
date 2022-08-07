package com.example.greapp.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.greapp.models.DailyActivity

@Dao
interface DailyActivityDao {
    @Transaction
    @Query ("SELECT * FROM DailyActivity")
    fun getAll() : List<DailyActivity>

    @Insert
    suspend fun insert (vararg dailyActivity: DailyActivity)
}