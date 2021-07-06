package br.com.jv.lembreteponto.data

import android.content.SharedPreferences

class Preferences(
    private val sharedPreferences: SharedPreferences
) {

    fun save(key: String, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun get(key: String, defaultValue: String? = null) = sharedPreferences.getString(key, defaultValue)

    companion object {
        const val PREFERENCES_APP = "PREFERENCES_APP"
        const val KEY_ENTRADA = "KEY_ENTRADA"
        const val KEY_ALMOCO = "KEY_ALMOCO"
        const val KEY_RETORNO_ALMOCO = "KEY_RETORNO_ALMOCO"
        const val KEY_SAIDA = "KEY_SAIDA"
        const val KEY_SETTINGS_HOUR_ALMOCO = "KEY_SETTINGS_HOUR_ALMOCO"
        const val KEY_SETTINGS_PACKAGE_APP = "KEY_SETTINGS_PACKAGE_APP"
        const val KEY_SETTINGS_TOTAL_HOURS = "KEY_SETTINGS_TOTAL_HOURS"

    }
}