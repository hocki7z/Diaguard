package com.faltenreich.diaguard.backup.legacy.measurement

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.database.sqlite.getDouble
import com.faltenreich.diaguard.shared.database.sqlite.getLong

class LegacyBloodPressureSqliteDao(
    private val database: SqliteDatabase,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        val values = mutableListOf<MeasurementValue.Legacy>()
        database.query("pressure") {
            val id = getLong("_id") ?: return@query
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            getDouble("systolic")?.takeIf { it > 0 }?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        id = id,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_SYSTOLIC,
                        entryId = entryId,
                    )
                )
            }
            getDouble("diastolic")?.takeIf { it > 0 }?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        id = id,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_DIASTOLIC,
                        entryId = entryId,
                    )
                )
            }
        }
        return values
    }
}