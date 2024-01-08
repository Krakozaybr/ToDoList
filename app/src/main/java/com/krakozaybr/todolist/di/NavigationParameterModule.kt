package com.krakozaybr.todolist.di

import androidx.lifecycle.SavedStateHandle
import com.krakozaybr.todolist.di.qualifiers.TaskId
import com.krakozaybr.todolist.navigation.Screen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


// thanks https://medium.com/@i.write.code/android-compose-navigation-view-models-and-hilt-c824541bd8e

@Module
@InstallIn(ViewModelComponent::class)
class NavigationParameterModule {

    @Provides
    @TaskId
    @ViewModelScoped
    fun provideTaskId(savedStateHandle: SavedStateHandle): Int =
        savedStateHandle.get<Int>(Screen.TASK_ID_KEY)
            ?: throw IllegalStateException("Could not get the 'taskId' parameter")

}