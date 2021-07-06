package br.com.jv.lembreteponto.data.entities

data class SettingsModel(
    val urlOpen: String = "",
    val timeMinuteAlmoco: Int = 60,
    val totalTime: Int = 8,
)
