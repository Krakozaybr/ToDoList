package com.krakozaybr.todolist.navigation.nav_types

import android.os.Bundle
import androidx.navigation.NavType
import com.krakozaybr.todolist.presentation.toUtcEpochMillis
import com.krakozaybr.todolist.presentation.utcToLocalDate
import java.time.LocalDate

val LocalDateNavType = object : NavType<LocalDate>(false) {

    override fun get(bundle: Bundle, key: String): LocalDate {
        return bundle.getLong(key).utcToLocalDate()
    }

    override fun parseValue(value: String): LocalDate {
        return value.toLong().utcToLocalDate()
    }

    override fun put(bundle: Bundle, key: String, value: LocalDate) {
        bundle.putLong(key, value.toUtcEpochMillis())
    }
}