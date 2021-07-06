package br.com.jv.lembreteponto.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import br.com.jv.lembreteponto.R
import br.com.jv.lembreteponto.data.entities.ScheduleNotification
import br.com.jv.lembreteponto.view.activity.MainActivity


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java), 0
        )
        intent.getBundleExtra(EXTRA_KEY)?.getParcelable<ScheduleNotification>(EXTRA_KEY)
            ?.let { schedule ->
                val notification = NotificationCompat.Builder(
                    context,
                    context.getString(R.string.channel_notification)
                )
                    .setSmallIcon(R.drawable.ic_clock)
                    .setContentIntent(contentIntent)
                    .setContentTitle(schedule.title)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentText(schedule.description)
                // Show notification
                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(schedule.id, notification.build())
            }
    }

    companion object {
        const val EXTRA_KEY = "schedule"
    }
}