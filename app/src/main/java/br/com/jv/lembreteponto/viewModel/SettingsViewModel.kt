package br.com.jv.lembreteponto.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.jv.lembreteponto.data.Preferences
import br.com.jv.lembreteponto.data.entities.SettingsModel

class SettingsViewModel(private val preferences: Preferences) : ViewModel() {
    val currentSettings = MutableLiveData<SettingsModel>()
    val successSave = MutableLiveData<Any>()

    fun init() {
        currentSettings.value = SettingsModel(
            urlOpen = preferences.get(Preferences.KEY_SETTINGS_PACKAGE_APP) ?: "",
            timeMinuteAlmoco = preferences.get(Preferences.KEY_SETTINGS_HOUR_ALMOCO)?.toInt() ?: 60,
            totalTime =  preferences.get(Preferences.KEY_SETTINGS_TOTAL_HOURS)?.toInt() ?: 8
        )
    }

    fun saveChanges(packageApp: String, hourAlmoco: String, totalHours: String) {
        preferences.save(Preferences.KEY_SETTINGS_PACKAGE_APP, packageApp)
        preferences.save(Preferences.KEY_SETTINGS_HOUR_ALMOCO, hourAlmoco)
        preferences.save(Preferences.KEY_SETTINGS_TOTAL_HOURS, totalHours)
        successSave.value = null
    }
}