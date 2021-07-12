package br.com.jv.lembreteponto.useCase

import android.content.Context
import br.com.jv.lembreteponto.R
import br.com.jv.lembreteponto.data.Preferences
import br.com.jv.lembreteponto.data.Preferences.Companion.KEY_ALMOCO
import br.com.jv.lembreteponto.data.Preferences.Companion.KEY_SETTINGS_HOUR_ALMOCO
import br.com.jv.lembreteponto.data.entities.ClockType
import br.com.jv.lembreteponto.data.entities.UpdateTime
import br.com.jv.lembreteponto.ext.*
import java.util.*

class ClockUseCase(
    private val preferences: Preferences,
    private val context: Context
) {

    fun verifyIfCanCalculatePreviewHoursAlmoco(): Boolean {
        return preferences.get(KEY_ALMOCO, null) != null
    }

    fun verifyIfCanCalculatePreviewHoursRetornoAlmoco(): Boolean {
        return preferences.get(KEY_ALMOCO, null) != null
                && preferences.get(Preferences.KEY_ENTRADA) != null
                && preferences.get(Preferences.KEY_RETORNO_ALMOCO) != null
    }

    fun calculatePreviewAlmoco(): String {
        val timeAlmoco =
            preferences.get(KEY_SETTINGS_HOUR_ALMOCO)?.toInt() ?: INTERVAL_ALMOCO_DEFAULT
        val hourClockAlmoco = preferences.get(KEY_ALMOCO, null)!!.toDate()
        val nextHourRetornoAlmoco = hourClockAlmoco.incrementMinutes(timeAlmoco)
        return nextHourRetornoAlmoco.dateToString()
    }

    fun previewTimeRetornoAlmocoWithSettings(): Date {
        val intervalAlmoco =
            (preferences.get(KEY_SETTINGS_HOUR_ALMOCO))?.toInt() ?: INTERVAL_ALMOCO_DEFAULT
        val timeAlmoco = preferences.get(KEY_ALMOCO)?.toDate()
        return timeAlmoco?.incrementMinutes(intervalAlmoco)!!
    }

    fun calculatePreviewSaida(timeRetornoAlmoco: Date?): String {
        val timeEntrada = preferences.get(Preferences.KEY_ENTRADA)?.toDate()
        val timeAlmoco = preferences.get(KEY_ALMOCO)?.toDate()
        val totalExpedienteMinutos =
            (preferences.get(Preferences.KEY_SETTINGS_TOTAL_HOURS)?.toInt()
                ?: TOTAL_EXPEDIENTE).hoursToMinutes()
        val totalTimeAlmoco = timeAlmoco?.getMinutesDiff(timeRetornoAlmoco)
        val workedHours = timeEntrada?.getMinutesDiff(timeRetornoAlmoco)?.minus(totalTimeAlmoco!!)
        val result = totalExpedienteMinutos.minus(workedHours!!)
        val resultDate = Calendar.getInstance().apply {
            time = timeRetornoAlmoco!!
            add(Calendar.MINUTE, result)
        }.time
        return resultDate.dateToString()
    }

    fun isAfter(date: Date): Boolean {
        return Date().dateToString().toDate().after(Date(date.time).dateToString().toDate())
    }

    fun getDescriptionMessageAlert(clockType: ClockType, timeToUpdate: String) =
        if (clockType is ClockType.Almoco) generateMessageAlmoco(timeToUpdate) else generateMessageSaida(
            timeToUpdate
        )


    private fun generateMessageSaida(timeToUpdate: String) =
        context.getString(R.string.message_notification_saida, timeToUpdate)


    private fun generateMessageAlmoco(timeToUpdate: String) =
        context.getString(R.string.message_notification_almoco, timeToUpdate)

    fun getTitleAlert() = context.getString(R.string.notification_title)

    companion object {
        private const val INTERVAL_ALMOCO_DEFAULT = 60
        private const val TOTAL_EXPEDIENTE = 8
    }
}