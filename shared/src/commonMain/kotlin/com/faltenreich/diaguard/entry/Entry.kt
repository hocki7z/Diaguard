package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.datetime.DateTime

data class Entry(
    val id: Long,
    val dateTime: DateTime,
    val note: String?,
)