package com.example.greapp.repositories

import androidx.room.*
import com.example.greapp.models.DailyActivity
import com.example.greapp.models.AllTimeActivity
import java.util.*

@Dao
interface AllTimeActivityDao {

    @Transaction
    @Query("SELECT * FROM AllTimeActivity")
    fun getAll() : List<AllTimeActivity>

    @Query("INSERT INTO AllTimeActivity SELECT * FROM  DailyActivity WHERE expectedEndTime BETWEEN :first - 86400000 AND :first")
    fun populateForYesterday(first: Long)

    @Query("UPDATE AllTimeActivity SET markedFinishedTime = :time WHERE id = :id")
    fun update(id : Int, time: Date)

    @Query(
        "UPDATE AllTimeActivity SET startTime = startTime + :time, expectedEndTime = expectedEndTime + :time" +
            " WHERE startTime BETWEEN :start AND :end")
    fun update(time : Long, start: Long, end: Long)

    @Insert
    fun insert (vararg allTimeActivity: AllTimeActivity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(objects: List<DailyActivity>)

    @Delete
    fun delete (allTimeActivity: AllTimeActivity)

}