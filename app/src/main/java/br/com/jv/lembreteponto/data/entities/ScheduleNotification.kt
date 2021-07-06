package br.com.jv.lembreteponto.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ScheduleNotification(
    val title: String,
    val id: Int,
    val description: String,
    val timeAfter: Date
):Parcelable