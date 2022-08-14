package com.example.greapp.repositories

import androidx.room.*
import com.example.greapp.models.DailyActivity
import java.util.*

@Dao
interface DailyActivityDao {
    @Transaction
    @Query ("SELECT * FROM DailyActivity")
    fun getAll() : List<DailyActivity>

    @Query("SELECT * FROM DAILYACTIVITY WHERE startTime < :midnight")
    fun getPast(midnight : Long) : List<DailyActivity>

    @Query("SELECT * FROM DAILYACTIVITY WHERE startTime IS NULL AND expectedEndTime IS NULL")
    fun getNoTime() : List<DailyActivity>

    @Query("SELECT * FROM DAILYACTIVITY  WHERE expectedEndTime BETWEEN :first - 86400000 AND :first")
    fun getYesterdays(first: Long) : List<DailyActivity>

    @Query("UPDATE DailyActivity SET markedFinishedTime = :time WHERE id = :id")
    fun update(id : Int, time: Date)

    @Query("""UPDATE DailyActivity SET startTime = startTime + :time, expectedEndTime = expectedEndTime + :time
             WHERE startTime BETWEEN :start AND :end""")
    fun update(time : Long, start: Long, end: Long)

    @Query("""UPDATE DailyActivity SET startTime = startTime + :time, 
        expectedEndTime = expectedEndTime + :time WHERE startTime BETWEEN :start AND :end""")
    fun updateOnStartTime(time : Long, start: Long, end: Long)

    @Insert
    fun insert (vararg dailyActivity: DailyActivity)

    @Delete
    fun delete (activity: DailyActivity)

    @Query("DELETE FROM DailyActivity WHERE startTime < :midnight")
    fun deletePast(midnight : Long) : Int

    @Query("DELETE FROM AllTimeActivity WHERE expectedEndTime BETWEEN :first - 86400000 AND :first")
    fun deleteYesterdays(first: Long)


}