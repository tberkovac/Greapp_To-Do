package com.example.greapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class DailyActivity(
    @ColumnInfo(name = "name") @SerializedName("name") val name : String,
    @ColumnInfo(name = "note") @SerializedName("note") val note : String,
    @SerializedName("startTime") val startTime : Date,
    @SerializedName("expectedEndTime") val expectedEndTime : Date,
    @SerializedName("markedFinishedTime") val markedFinishedTime : Date?

    ) : Parcelable {
    @PrimaryKey(autoGenerate = true) var id = 0
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(Date::class.java.classLoader) as Date,
        parcel.readValue(Date::class.java.classLoader) as Date,
        parcel.readValue(Date::class.java.classLoader) as Date
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(note)
        parcel.writeDate(startTime)
        parcel.writeDate(expectedEndTime)
        parcel.writeDate(markedFinishedTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun Parcel.writeDate(date: Date?) {
        writeLong(date?.time ?: -1)
    }

    fun Parcel.readDate(): Date? {
        val long = readLong()
        return if (long != -1L) Date(long) else null
    }


    companion object CREATOR : Parcelable.Creator<DailyActivity> {
        override fun createFromParcel(parcel: Parcel): DailyActivity {
            return DailyActivity(parcel)
        }

        override fun newArray(size: Int): Array<DailyActivity?> {
            return arrayOfNulls(size)
        }
    }
}
