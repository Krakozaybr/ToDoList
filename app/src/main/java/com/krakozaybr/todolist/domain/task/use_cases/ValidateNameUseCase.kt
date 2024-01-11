package com.krakozaybr.todolist.domain.task.use_cases

import javax.inject.Inject

class ValidateNameUseCase @Inject constructor() {

    operator fun invoke(name: String): NameValidationResult {
        if (name.isEmpty()) {
            return NameValidationResult.EMPTY
        }

        if (name.isBlank()) {
            return NameValidationResult.BLANK
        }

        return NameValidationResult.OK
    }



}

enum class NameValidationResult {
    OK,
    EMPTY,
    BLANK
}