package com.wngud.oneminutestart.data.db.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    fun getAllTasks(): Flow<List<TaskEntity>>
}