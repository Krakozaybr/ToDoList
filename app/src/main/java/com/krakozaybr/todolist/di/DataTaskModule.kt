package com.krakozaybr.todolist.di

import com.krakozaybr.todolist.data.TestTaskRepository
import com.krakozaybr.todolist.domain.task.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataTaskModule {

    @Binds
    fun bindTaskRepository(repo: TestTaskRepository): TaskRepository

}