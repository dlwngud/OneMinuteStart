package com.wngud.oneminutestart.utils

fun extractTimeComponents(timeString: String?): String {
    return if (timeString.isNullOrEmpty()) {
        "오전 1 0"
    } else {
        val (ampm, time) = timeString.split(" ")
        val (hour, minute) = time.split(":")
        "$ampm $hour $minute"
    }
}