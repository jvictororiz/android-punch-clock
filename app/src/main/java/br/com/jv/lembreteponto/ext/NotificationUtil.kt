package br.com.jv.lembreteponto.ext

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import br.com.jv.lembreteponto.data.entities.ScheduleNotification
import br.com.jv.lembreteponto.service.NotificationReceiver
import br.com.jv.lembreteponto.service.NotificationReceiver.Companion.EXTRA_KEY
import java.util.*

fun scheduleNotification(context: Context, schedule: ScheduleNotification) {
    val intent = Intent(context, NotificationReceiver::class.java)
    val bundle = Bundle()
    bundle.putParcelable(EXTRA_KEY, schedule)
    intent.putExtra(EXTRA_KEY, bundle)
    val pending =
        PendingIntent.getBroadcast(
            context,
            schedule.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, schedule.timeAfter.hours())
        set(Calendar.MINUTE, schedule.timeAfter.minutes())
        set(Calendar.SECOND, 0)
    }
    val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    manager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pending
    )
}

fun cancelNotification(context: Context, idNotification: Int) {
    val pending =
        PendingIntent.getBroadcast(
            context,
            idNotification,
            Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    // Cancel notification
    val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    manager.cancel(pending)
}