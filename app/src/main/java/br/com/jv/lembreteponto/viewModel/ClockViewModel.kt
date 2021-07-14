package br.com.jv.lembreteponto.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.jv.lembreteponto.data.Preferences
import br.com.jv.lembreteponto.data.Preferences.Companion.KEY_ALMOCO
import br.com.jv.lembreteponto.data.Preferences.Companion.KEY_ENTRADA
import br.com.jv.lembreteponto.data.Preferences.Companion.KEY_RETORNO_ALMOCO
import br.com.jv.lembreteponto.data.Preferences.Companion.KEY_SAIDA
import br.com.jv.lembreteponto.data.Preferences.Companion.KEY_SETTINGS_PACKAGE_APP
import br.com.jv.lembreteponto.data.entities.ClockType
import br.com.jv.lembreteponto.data.entities.ScheduleNotification
import br.com.jv.lembreteponto.data.entities.UpdateTime
import br.com.jv.lembreteponto.ext.convertTimeInId
import br.com.jv.lembreteponto.ext.dateToString
import br.com.jv.lembreteponto.ext.toDate
import br.com.jv.lembreteponto.ext.waitTime
import br.com.jv.lembreteponto.useCase.ClockUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class ClockViewModel(
    private val preferences: Preferences,
    private val useCase: ClockUseCase
) : ViewModel() {

    private var timer: CountDownTimer? = null
    val clockUpdateView = MutableLiveData<UpdateTime>()
    val clockUpdateHintViewAlmoco = MutableLiveData<String>()
    val clockUpdateHintViewSaida = MutableLiveData<String>()
    val clearClock = MutableLiveData<ClockType>()
    val scheduleNotificationEvent = MutableLiveData<ScheduleNotification>()
    val hideAlertEvent = MutableLiveData<Any>()
    val cancelScheduleNotificationEvent = MutableLiveData<Int>()
    val goToScreenEvent = MutableLiveData<String>()


    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            ClockType.getAllTypes().forEach { type ->
                viewModelScope.launch(Dispatchers.Main) {
                    when (type) {
                        is ClockType.Entrada -> {
                            preferences.get(KEY_ENTRADA)?.let {
                                clockUpdateView.value = (UpdateTime(it, type))
                            }
                        }
                        is ClockType.Almoco -> {
                            preferences.get(KEY_ALMOCO)?.let {
                                clockUpdateView.value = (UpdateTime(it, type))
                                showClockUpdateHintViewAlmoco()
                                showclockUpdateHintViewSaida()
                            }
                        }
                        is ClockType.RetornoAlmoco -> {
                            preferences.get(KEY_RETORNO_ALMOCO)?.let {
                                clockUpdateView.value = (UpdateTime(it, type))
                                showclockUpdateHintViewSaida()
                            }
                        }
                        is ClockType.Saida -> {
                            preferences.get(KEY_SAIDA)?.let {
                                clockUpdateView.value = (UpdateTime(it, type))
                            }
                        }
                        else -> {
                        }
                    }
                }
            }
            viewModelScope.launch(Dispatchers.Main) {
                hideAlertEvent.value = null
            }
        }
    }

    fun tapOnClock() {
        changeTimeByClockType(getNextClockType(), Date().dateToString(), true)
    }

    fun tapOnClear() {
        preferences.save(KEY_ENTRADA, null)
        preferences.save(KEY_ALMOCO, null)
        preferences.save(KEY_RETORNO_ALMOCO, null)
        preferences.save(KEY_SAIDA, null)
        cancelAlarmsSchedules()
        ClockType.getAllTypes().forEach {
            clearClock.value = it
        }
    }

    fun changeTime(clockType: ClockType, hourOfDay: Int, minute: Int) {
        val newTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }.time
        changeTimeByClockType(clockType, newTime.dateToString(), false)
    }

    private fun changeTimeByClockType(
        clockType: ClockType,
        timeToUpdate: String,
        openApp: Boolean
    ) {
        when (clockType) {
            is ClockType.Entrada -> {
                saveAndUpdateScreen(clockType, KEY_ENTRADA, timeToUpdate)
                calculateAndShowPreviewHour()
            }
            is ClockType.Almoco -> {
                saveAndUpdateScreen(clockType, KEY_ALMOCO, timeToUpdate)
                calculateAndShowPreviewHour()
            }
            is ClockType.RetornoAlmoco -> {
                saveAndUpdateScreen(clockType, KEY_RETORNO_ALMOCO, timeToUpdate)
                calculateAndShowPreviewHour()
            }
            is ClockType.Saida -> {
                saveAndUpdateScreen(clockType, KEY_SAIDA, timeToUpdate)
                calculateAndShowPreviewHour()
            }
            else -> {
                tapOnClear()
                return
            }
        }
        if (openApp) {
            goToThirdAppSave()
        }
    }


    private fun cancelAlarmsSchedules() {
        clockUpdateHintViewAlmoco.value?.let {
            cancelScheduleNotificationEvent.value = it.convertTimeInId()
        }
        clockUpdateHintViewSaida.value?.let {
            cancelScheduleNotificationEvent.value = it.convertTimeInId()
        }
    }

    private fun scheduleAllNotification() {
        clockUpdateHintViewSaida.value?.let {
            scheduleNotifications(it, ClockType.Saida)
        }
        clockUpdateHintViewAlmoco.value?.let {
            scheduleNotifications(it, ClockType.Almoco)
        }
    }

    private fun scheduleNotifications(timeToUpdate: String, clockType: ClockType) {
        if (useCase.isAfter(timeToUpdate.toDate())) {
            return
        }
        val title = useCase.getTitleAlert()
        val description = useCase.getDescriptionMessageAlert(clockType, timeToUpdate)

        scheduleNotificationEvent.value = ScheduleNotification(
            title = title,
            description = description,
            id = timeToUpdate.convertTimeInId(),
            timeAfter = timeToUpdate.toDate()
        )
    }

    private fun calculateAndShowPreviewHour() {
        cancelAlarmsSchedules()
        val currentClockType = getCurrentClockType()
        if (currentClockType is ClockType.Almoco) {
            clockUpdateHintViewAlmoco.value = useCase.calculatePreviewAlmoco()
            scheduleAllNotification()

        } else if (currentClockType is ClockType.RetornoAlmoco) {
            showclockUpdateHintViewSaida()
            scheduleAllNotification()
        }
    }

    private fun showClockUpdateHintViewAlmoco() {
        if (useCase.verifyIfCanCalculatePreviewHoursAlmoco()) {
            val timeMinutes = useCase.previewTimeRetornoAlmocoWithSettings()
            clockUpdateHintViewSaida.value = useCase.calculatePreviewSaida(timeMinutes)
        }
    }

    private fun showclockUpdateHintViewSaida() {
        if (useCase.verifyIfCanCalculatePreviewHoursRetornoAlmoco()) {
            val timeRetornoAlmoco = preferences.get(KEY_RETORNO_ALMOCO)?.toDate()
            clockUpdateHintViewSaida.value = useCase.calculatePreviewSaida(timeRetornoAlmoco)
        }
    }

    private fun goToThirdAppSave() {
        waitTime(TIME_TO_START_SCREEN) {
            preferences.get(KEY_SETTINGS_PACKAGE_APP)?.let {
                goToScreenEvent.value = it
            }
        }
    }

    private fun saveAndUpdateScreen(clockType: ClockType, type: String, time: String) {
        clockUpdateView.value = UpdateTime(
            time = time,
            clockType = clockType
        )
        preferences.save(type, time)
    }

    private fun getNextClockType(): ClockType {
        val entrada = preferences.get(KEY_ENTRADA)
        val almoco = preferences.get(KEY_ALMOCO)
        val retornoAlmoco = preferences.get(KEY_RETORNO_ALMOCO)
        val saida = preferences.get(KEY_SAIDA)
        return ClockType.nextClockTypeTime(
            entrada,
            almoco,
            retornoAlmoco,
            saida
        )
    }

    private fun getCurrentClockType(): ClockType {
        val entrada = preferences.get(KEY_ENTRADA)
        val almoco = preferences.get(KEY_ALMOCO)
        val retornoAlmoco = preferences.get(KEY_RETORNO_ALMOCO)
        val saida = preferences.get(KEY_SAIDA)
        return ClockType.currentClockTypeTime(
            entrada,
            almoco,
            retornoAlmoco,
            saida
        )
    }

    override fun onCleared() {
        timer?.onFinish()
        super.onCleared()
    }

    companion object {
        private const val TIME_TO_START_SCREEN = 800L
    }
}
