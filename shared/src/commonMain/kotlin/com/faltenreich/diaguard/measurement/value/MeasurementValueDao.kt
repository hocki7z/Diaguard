package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementValueDao {

    fun create(
        createdAt: DateTime,
        value: Double,
        typeId: Long,
        entryId: Long,
    )

    fun getLastId(): Long?

    fun observeByEntryId(entryId: Long): Flow<List<MeasurementValue>>

    fun observeLatestByPropertyId(propertyId: Long): Flow<MeasurementValue?>

    fun getByEntryId(entryId: Long): List<MeasurementValue>

    fun observeCountByPropertyId(propertyId: Long): Flow<Long>

    fun observeCountByTypeId(typeId: Long): Flow<Long>

    fun update(
        id: Long,
        updatedAt: DateTime,
        value: Double,
    )

    fun deleteById(id: Long)
}