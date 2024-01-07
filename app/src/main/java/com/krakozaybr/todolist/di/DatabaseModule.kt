package com.krakozaybr.todolist.di

import android.content.Context
import com.krakozaybr.todolist.data.db.AppDatabase
import com.krakozaybr.todolist.data.db.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    companion object {
        @Provides
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return AppDatabase.getInstance(context)
        }

        @Provides
        fun provideTaskDao(db: AppDatabase): TaskDao {
            return db.taskDao()
        }
    }

}