package com.faltenreich.diaguard.shared.datetime.kotlinx

import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class KotlinxDateTimeApi : DateTimeApi {

    override fun now(): DateTime {
        return KotlinxDateTime(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    }

    override fun convertDateTimeToIsoString(dateTime: DateTime): String {
        return dateTime.toString()
    }

    override fun convertIsoStringToDateTime(isoString: String): DateTime {
        return KotlinxDateTime(LocalDateTime.parse(isoString))
    }
}