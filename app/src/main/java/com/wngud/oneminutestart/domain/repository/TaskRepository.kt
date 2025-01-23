package com.wngud.oneminutestart.domain.repository

import com.wngud.oneminutestart.domain.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    suspend fun saveTask(task: Task)
}