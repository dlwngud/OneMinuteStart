package com.wngud.oneminutestart.domain

import android.os.Parcelable
import com.wngud.oneminutestart.data.db.local.TaskEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: Long = 0L,                    // 고유 ID
    val title: String = "",              // 작업 제목
    val reminderTime: String = "",      // 알림 시간 (HH:mm 포맷)
    val isCompletedOneMinute: Boolean = false, // 완료 여부
): Parcelable

fun Task.toTaskEntity(): TaskEntity = TaskEntity(
    id = this.id,
    title = this.title,
    reminderTime = this.reminderTime,
    isCompletedOneMinute = this.isCompletedOneMinute
)