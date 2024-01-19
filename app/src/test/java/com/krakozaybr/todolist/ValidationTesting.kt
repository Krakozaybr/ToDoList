package com.krakozaybr.todolist

import com.krakozaybr.todolist.domain.task.use_cases.NameValidationResult
import com.krakozaybr.todolist.domain.task.use_cases.TimeRangeValidation
import com.krakozaybr.todolist.domain.task.use_cases.ValidateNameUseCase
import com.krakozaybr.todolist.domain.task.use_cases.ValidateTimeRangeUseCase
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalTime

class ValidationTesting {

    private val validateName = ValidateNameUseCase()
    private val validateTimeRange = ValidateTimeRangeUseCase()

    @Test
    fun testEmptyNameValidation() {
        val name = ""

        val result = validateName(name)

        assertEquals(result, NameValidationResult.EMPTY)
    }

    @Test
    fun testBlankNameValidation() {
        val name = "\t\n "

        val result = validateName(name)

        assertEquals(result, NameValidationResult.BLANK)
    }

    @Test
    fun testCorrectNameValidation() {
        val name = "Some name"

        val result = validateName(name)

        assertEquals(result, NameValidationResult.OK)
    }

    @Test
    fun testEqualRangeValidation() {
        val start = LocalTime.of(12, 0, 0)
        val end = LocalTime.of(12, 0, 0)

        val result = validateTimeRange(start, end)

        assertEquals(result, TimeRangeValidation.OK)
    }

    @Test
    fun testIncorrectRangeValidation() {
        val start = LocalTime.of(12, 0, 1)
        val end = LocalTime.of(12, 0, 0)

        val result = validateTimeRange(start, end)

        assertEquals(result, TimeRangeValidation.START_AFTER_FINISH)
    }

    @Test
    fun testCorrectRangeValidation() {
        val start = LocalTime.of(12, 0, 0)
        val end = LocalTime.of(12, 0, 1)

        val result = validateTimeRange(start, end)

        assertEquals(result, TimeRangeValidation.OK)
    }

}
