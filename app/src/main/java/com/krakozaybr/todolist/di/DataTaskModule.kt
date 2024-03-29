package com.krakozaybr.todolist.di

import com.krakozaybr.todolist.data.DatabaseTaskRepository
import com.krakozaybr.todolist.domain.task.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataTaskModule {

    @Singleton
    @Binds
    fun bindTaskRepository(repo: DatabaseTaskRepository): TaskRepository

}