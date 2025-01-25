package com.wngud.oneminutestart.domain.repository

import com.wngud.oneminutestart.domain.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    fun getTaskById(id: Long): Flow<Task>

    suspend fun saveTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)
}