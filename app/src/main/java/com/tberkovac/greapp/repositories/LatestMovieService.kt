package com.tberkovac.greapp.repositories

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tberkovac.greapp.models.AllTimeActivity
import com.tberkovac.greapp.models.DailyActivity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.IOException
import java.net.MalformedURLException
import java.util.*

//moze posluziti poslije za notifikacije
class LatestMovieService : Service() {

    //za spriječavanje prekida u slučaja Daze
    private var wakeLock: PowerManager.WakeLock? = null
    //oznaka da je srevis pokrenut
    private var isServiceStarted = false


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
     //   val notification = createNotification()
     //   startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startService()
        // servis će se restartovati ako ovo vratimo
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "LATEST MOVIE SERVICE CHANNEL"
        //Različite metode kreiranja notifikacije
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager
            //Kreiramo notifikacijski kanal - notifikaicje se šalju na isti kanal
            val channel = NotificationChannel(
                notificationChannelId,
                "Greapp Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
            //Definišemo karakteristike notifikacije
                it.description = "Greapp Notification Channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200,
                    400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }
        //gradimo notifikaciju u ovisnosti od verzije
        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)
//Kreira se notifikacija koja će se prikazati kada se servis pokrene
        return builder.build()
    }


    private fun startService() {
    //U slučaju da se ponovo pokrene ne mora se pozivati ova metoda
        if (isServiceStarted) return
        //Postavimo da je servis pokrenut
        isServiceStarted = true
        //Koristit ćemo wakeLock da spriječimo gašenje servisa
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "LatestMovieService::lock").apply {
                acquire()
            }
        }
        //Beskonačna petlja koja dohvati podatke svakih sat vremena
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                launch(Dispatchers.IO) {
                    getData()
                }
                //Sačekaj dan prije nego se ponovo pokreneš
                delay(86400000)
            }
        }
    }

    private fun getData() {
        try {
            val notifyIntent = Intent(this, DailyActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val notification = NotificationCompat.Builder(baseContext, "LATEST MOVIE SERVICE CHANNEL").apply{
                setSmallIcon(android.R.drawable.stat_notify_sync)
                setContentTitle("Zavrseno")
                setContentText("Prebacene u arhivu aktivnosti")
                setContentIntent(notifyPendingIntent)
                setOngoing(false)
                build()
            }
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(123, notification.build())
            }

            val midnight = setDateToMidnight()

            var cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1)


                val db = GreappDB.getInstance(baseContext)
                var pastActivities = db.dailyActivityDao().getPast(midnight.time)
                val allTimePastMapped = pastActivities.map { AllTimeActivity(it.name,it.note,it.startTime,it.expectedEndTime,it.markedFinishedTime) }
                db.allTimeActivityDao().insertAll(allTimePastMapped)
                db.dailyActivityDao().deletePast(midnight.time)
                db.dailyActivityDao().insert(DailyActivity("Sa Servisa", "", Calendar.getInstance().time,cal.time,null))
                Log.v("Servissss", "poslanooooo")

        } catch (e: MalformedURLException) {
            Log.v("MalformedURLException", "Cannot open HttpURLConnection")
        } catch (e: IOException) {
            Log.v("IOException", "Cannot read stream")
        } catch (e: JSONException) {
            Log.v("IOException", "Cannot parse JSON")
        }


    }

    fun setDateToMidnight() : Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        return cal.time
    }
}