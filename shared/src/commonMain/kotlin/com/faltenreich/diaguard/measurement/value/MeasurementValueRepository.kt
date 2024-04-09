package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.deep
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class MeasurementValueRepository(
    private val dao: MeasurementValueDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        value: Double,
        propertyId: Long,
        entryId: Long,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            value = value,
            propertyId = propertyId,
            entryId = entryId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(
        value: Double,
        propertyId: Long,
        entryId: Long,
    ): Long {
        val now = dateTimeFactory.now()
        return create(
            createdAt = now,
            updatedAt = now,
            value = value,
            propertyId = propertyId,
            entryId = entryId,
        )
    }

    fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): Flow<List<MeasurementValue>> {
        return dao.observeByDateRange(startDateTime, endDateTime)
    }

    fun observeLatestByCategoryId(categoryId: Long): Flow<MeasurementValue?> {
        return dao.observeLatestByCategoryId(categoryId)
    }

    fun getByEntryId(entryId: Long): List<MeasurementValue> {
        return dao.getByEntryId(entryId)
    }

    fun observeByCategoryId(categoryId: Long): Flow<Long> {
        return dao.observeByCategoryId(categoryId)
    }

    fun observeByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<List<MeasurementValue>> {
        return dao.observeByCategoryId(categoryId, minDateTime, maxDateTime)
    }

    fun observeAverageByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?> {
        return dao.observeAverageByCategoryId(categoryId, minDateTime, maxDateTime)
    }

    fun getAverageByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Double? {
        return dao.getAverageByPropertyId(propertyId, minDateTime, maxDateTime)
    }

    fun observeCountByPropertyId(propertyId: Long): Flow<Long> {
        return dao.observeCountByPropertyId(propertyId)
    }

    fun countByCategoryId(categoryId: Long): Long {
        return dao.countByCategoryId(categoryId)
    }

    fun update(
        id: Long,
        value: Double,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            value = value,
        )
    }

    fun update(value: MeasurementValue) {
        update(
            id = value.id,
            value = value.value,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}

fun List<MeasurementValue>.deep(
    entry: Entry,
    propertyRepository: MeasurementPropertyRepository = inject(),
): List<MeasurementValue> {
    return map { value ->
        value.apply {
            val property = checkNotNull(propertyRepository.getById(value.propertyId))
            this.property = property.deep()
            this.entry = entry
        }
    }
}