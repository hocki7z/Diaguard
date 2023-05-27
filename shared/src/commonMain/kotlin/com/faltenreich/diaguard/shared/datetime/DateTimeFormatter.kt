package com.faltenreich.diaguard.shared.datetime

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.primitive.format
import dev.icerock.moko.resources.compose.stringResource

class DateTimeFormatter {

    fun formatDateTime(dateTime: DateTime): String {
        return dateTime.run {
            "%s %s".format(
                formatDate(date),
                formatTime(time),
            )
        }
    }

    fun formatDate(date: Date): String {
        return date.run {
            "%02d.%02d.%04d".format(
                dayOfMonth,
                monthOfYear,
                year,
            )
        }
    }

    fun formatDayOfMonth(date: Date): String {
        return "%02d".format(date.dayOfMonth)
    }

    @Composable
    fun formatDayOfWeek(date: Date): String {
        return stringResource(date.dayOfWeek.abbreviation)
    }

    fun formatTime(time: Time): String {
        return time.run {
            "%02d:%02d".format(
                hourOfDay,
                minuteOfHour,
            )
        }
    }
}