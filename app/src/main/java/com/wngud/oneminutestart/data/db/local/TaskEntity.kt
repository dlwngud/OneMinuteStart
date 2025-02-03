package com.wngud.oneminutestart.data.db.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wngud.oneminutestart.domain.Task

@Entity(tableName = "task_table4")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,       // 고유 ID
    val title: String,                                        // 작업 제목
    val reminderTime: String,                                // 알림 시간 (HH:mm 포맷)
    val isCompletedOneMinute: Boolean,                // 완료 여부
    val elapsedTime: Long
)

fun TaskEntity?.toTask(): Task {
    return if (this == null) {
        Task()
    } else {
        Task(
            id = id,
            title = title,
            reminderTime = reminderTime,
            isCompletedOneMinute = isCompletedOneMinute,
            elapsedTime = elapsedTime
        )
    }
}