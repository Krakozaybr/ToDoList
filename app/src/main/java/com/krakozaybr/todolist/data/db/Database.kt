package com.krakozaybr.todolist.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.krakozaybr.todolist.data.db.dao.TaskDao
import com.krakozaybr.todolist.data.db.models.TaskDbModel


@Database(
    entities = [TaskDbModel::class], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        private const val DATABASE_NAME = "todo_list.db"
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun getInstance(application: Application): AppDatabase {
            instance?.let {
                return it
            }
            synchronized(LOCK) {
                instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                instance = db
                return db
            }
        }
    }

}