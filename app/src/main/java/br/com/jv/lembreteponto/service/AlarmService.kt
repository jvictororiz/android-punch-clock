package br.com.jv.lembreteponto.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import br.com.jv.lembreteponto.data.entities.ScheduleNotification
import br.com.jv.lembreteponto.view.activity.MainActivity

class AlarmService : Service() {
    private var vibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val pattern = longArrayOf(0, 2000, 1000, 2000, 1000, 2000, 1000, 2000)

        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))

        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), 0
        )

        intent.getBundleExtra(NotificationReceiver.EXTRA_KEY)?.getParcelable<ScheduleNotification>(
            NotificationReceiver.EXTRA_KEY
        )?.let { schedule ->
            val notification = NotificationCompat.Builder(
                this,
                getString(br.com.jv.lembreteponto.R.string.channel_notification)
            ).setSmallIcon(br.com.jv.lembreteponto.R.drawable.ic_clock)
                .setContentIntent(contentIntent)
                .setContentTitle(schedule.title)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentText(schedule.description)
                .build()
            startForeground(schedule.id, notification)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator?.cancel()
        stopForeground(true)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}