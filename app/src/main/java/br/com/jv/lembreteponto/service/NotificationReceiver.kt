package br.com.jv.lembreteponto.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import br.com.jv.lembreteponto.data.entities.ScheduleNotification


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val extra = intent.getBundleExtra(EXTRA_KEY)?.getParcelable<ScheduleNotification>(EXTRA_KEY)
        val intentService = Intent(context, AlarmService::class.java)

        intentService.putExtra(EXTRA_KEY, Bundle().apply {
            putParcelable(EXTRA_KEY, extra)
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    companion object {
        const val EXTRA_KEY = "schedule"
    }
}