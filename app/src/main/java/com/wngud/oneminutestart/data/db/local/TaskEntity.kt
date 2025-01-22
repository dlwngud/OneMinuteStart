package com.wngud.oneminutestart.data.db.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wngud.oneminutestart.domain.Task

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,       // 고유 ID
    val title: String,                                        // 작업 제목
    val reminderTime: String?,                                // 알림 시간 (HH:mm 포맷)
    val isCompletedOneMinute: Boolean = false,                // 완료 여부
)

fun TaskEntity.toTask(): Task = Task(
    id = this.id,
    title = this.title,
    reminderTime = this.reminderTime,
    isCompletedOneMinute = this.isCompletedOneMinute
)