package com.wngud.oneminutestart.di

import android.content.Context
import androidx.room.Room
import com.wngud.oneminutestart.data.db.local.TaskDao
import com.wngud.oneminutestart.data.db.local.TaskDatabase
import com.wngud.oneminutestart.data.db.local.TaskDatabase.Companion.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "task_table2")
            .addMigrations(MIGRATION_1_2).build()

    @Provides
    @Singleton
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao = taskDatabase.taskDao()
}