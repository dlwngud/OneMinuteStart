package com.wngud.oneminutestart.data.repository

import com.wngud.oneminutestart.data.db.local.TaskDao
import com.wngud.oneminutestart.data.db.local.toTask
import com.wngud.oneminutestart.domain.Task
import com.wngud.oneminutestart.domain.repository.TaskRepository
import com.wngud.oneminutestart.domain.toTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { it.map { it.toTask() } }

    override suspend fun saveTask(task: Task) {
        taskDao.saveTask(task.toTaskEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toTaskEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.update(task.toTaskEntity())
    }

}