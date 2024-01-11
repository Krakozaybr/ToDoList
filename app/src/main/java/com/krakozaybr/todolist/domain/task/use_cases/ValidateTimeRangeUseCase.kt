package com.krakozaybr.todolist.domain.task.use_cases

import java.time.LocalTime
import javax.inject.Inject

class ValidateTimeRangeUseCase @Inject constructor() {

    operator fun invoke(start: LocalTime, finish: LocalTime): TimeRangeValidation {
        if (start.isAfter(finish)) {
            return TimeRangeValidation.START_AFTER_FINISH
        }

        return TimeRangeValidation.OK
    }

}

enum class TimeRangeValidation {
    OK,
    START_AFTER_FINISH
}