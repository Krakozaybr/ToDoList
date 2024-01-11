package com.krakozaybr.todolist.presentation.validation

import com.krakozaybr.todolist.R
import com.krakozaybr.todolist.domain.task.use_cases.NameValidationResult
import com.krakozaybr.todolist.domain.task.use_cases.NameValidationResult.BLANK
import com.krakozaybr.todolist.domain.task.use_cases.NameValidationResult.EMPTY
import com.krakozaybr.todolist.domain.task.use_cases.NameValidationResult.OK
import com.krakozaybr.todolist.domain.task.use_cases.TimeRangeValidation
import com.krakozaybr.todolist.presentation.validation.UiText
import com.krakozaybr.todolist.presentation.validation.ValidationResult
import javax.inject.Inject

class ValidationMapper @Inject constructor() {

    operator fun invoke(res: NameValidationResult): ValidationResult {
        return when (res) {
            OK -> ValidationResult(true)
            EMPTY -> ValidationResult(false, UiText.StringResource(R.string.error_name_empty))
            BLANK -> ValidationResult(false, UiText.StringResource(R.string.error_name_blank))
        }
    }

    operator fun invoke(res: TimeRangeValidation): ValidationResult {
        return when (res) {
            TimeRangeValidation.OK -> ValidationResult(true)
            TimeRangeValidation.START_AFTER_FINISH -> ValidationResult(
                false,
                UiText.StringResource(R.string.error_time_order)
            )
        }
    }

}