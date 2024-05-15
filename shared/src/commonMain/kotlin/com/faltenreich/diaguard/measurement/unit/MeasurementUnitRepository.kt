package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

class MeasurementUnitRepository(
    private val dao: MeasurementUnitDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        name: String,
        abbreviation: String,
        key: DatabaseKey.MeasurementUnit?,
        factor: Double,
        propertyId: Long,
    ): Long {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = key,
            name = name,
            abbreviation = abbreviation,
            factor = factor,
            propertyId = propertyId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getByKey(key: String): MeasurementUnit {
        return checkNotNull(dao.getByKey(key))
    }

    fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementUnit>> {
        return dao.observeByPropertyId(propertyId)
    }

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementUnit>> {
        return dao.observeByCategoryId(categoryId)
    }

    fun observeAll(): Flow<List<MeasurementUnit>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        name: String,
        abbreviation: String,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            abbreviation = abbreviation,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}