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

    @Query("UPDATE DailyActivity SET startTime = startTime + :time, expectedEndTime = expectedEndTime + :time" +
            " WHERE startTime BETWEEN :start AND :end")
    fun update(time : Long, start: Long, end: Long)

    @Insert
    fun insert (vararg dailyActivity: DailyActivity)

    @Delete
    fun delete (activity: DailyActivity)

}