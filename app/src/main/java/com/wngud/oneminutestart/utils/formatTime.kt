package com.wngud.oneminutestart.utils

fun formatTime(isLeadingZeroNeeded: Boolean = false, value: Int): String {
    return if (isLeadingZeroNeeded)
        String.format("%02d", value)
    else
        String.format("%2d", value)
}