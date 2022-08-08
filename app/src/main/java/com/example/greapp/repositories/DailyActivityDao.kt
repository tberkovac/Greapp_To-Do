package com.example.greapp.repositories

import androidx.room.*
import com.example.greapp.models.DailyActivity
import java.util.*

@Dao
interface DailyActivityDao {
    @Transaction
    @Query ("SELECT * FROM DailyActivity")
    fun getAll() : List<DailyActivity>

    @Query("UPDATE DailyActivity SET markedFinishedTime = :time WHERE id = :id")
    fun update(id : Int, time: Date)

    @Insert
    fun insert (vararg dailyActivity: DailyActivity)

    @Delete
    fun delete (activity: DailyActivity)




}