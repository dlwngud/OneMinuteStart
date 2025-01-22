package com.wngud.oneminutestart.domain

import com.wngud.oneminutestart.data.db.local.TaskEntity
data class Task(
    val id: Long,                    // 고유 ID
    val title: String,              // 작업 제목
    val reminderTime: String?,      // 알림 시간 (HH:mm 포맷)
    val isCompletedOneMinute: Boolean = false, // 완료 여부
)

fun Task.toTaskEntity(): TaskEntity = TaskEntity(
    id = this.id,
    title = this.title,
    reminderTime = this.reminderTime,
    isCompletedOneMinute = this.isCompletedOneMinute
)