package com.faltenreich.diaguard.backup.legacy.measurement

import com.faltenreich.diaguard.backup.legacy.Legacy
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.datetime.DateTime

data class MeasurementValueLegacy(
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val value: Double,
    val propertyKey: DatabaseKey.MeasurementProperty,
    val entryId: Long,
) : Legacy