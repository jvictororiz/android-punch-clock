package br.com.jv.lembreteponto.ext

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun String.toDate(pattern: String = "HH:mm"): Date {
    val format = SimpleDateFormat(pattern, Locale("pt", "BR"))
    return try {
        format.parse(this) ?: Date()
    } catch (e: ParseException) {
        throw java.lang.Exception()
    }
}

fun String.convertTimeInId() = replace(":", "").toInt()

fun Date.dateToString(pattern: String = "HH:mm"): String {
    return SimpleDateFormat(pattern, Locale("pt", "BR")).format(this)
}

fun Date.incrementMinutes(minutes: Int): Date = Calendar.getInstance().apply {
    time = this@incrementMinutes
    add(Calendar.MINUTE, minutes)
}.time

fun Date.getMinutesDiff(date: Date?): Int {
    val mills: Long = date!!.time - this@getMinutesDiff.time
    return TimeUnit.MILLISECONDS.toMinutes(mills).toInt()
}

fun Date.minutes(): Int {
    val calendar = Calendar.getInstance().apply { time = this@minutes }
    return calendar.get(Calendar.MINUTE)
}


fun Date.hours(): Int {
    val calendar = Calendar.getInstance().apply { time = this@hours }
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun Date.nowUntil(date: Date): Long {
    val timeOne = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, this@nowUntil.hours())
        set(Calendar.MINUTE, date.minutes())
    }.time
    val timeTwo = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, date.hours())
        set(Calendar.MINUTE, date.minutes())
    }.time


    return timeOne.time - timeTwo.time
}

fun Calendar.onlyTime() = apply {
    set(Calendar.YEAR, 0)
    set(Calendar.MONTH, 0)
    set(Calendar.DAY_OF_MONTH, 0)
    set(Calendar.MILLISECOND, 0)
}

fun Date.onlyTime(): Date = Calendar.getInstance().apply {
    time = this@onlyTime
    set(Calendar.YEAR, 0)
    set(Calendar.MONTH, 0)
    set(Calendar.DAY_OF_MONTH, 0)
    set(Calendar.MILLISECOND, 0)
}.time

fun Int.hoursToMinutes(): Int {
    return this * 60
}
