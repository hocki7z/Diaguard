package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementTypeUnitRepository(
    private val dao: MeasurementTypeUnitDao,
) {

    fun create(
        factor: Double,
        typeId: Long,
        unitId: Long,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            factor = factor,
            typeId = typeId,
            unitId = unitId,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByTypeId(typeId: Long): Flow<List<MeasurementTypeUnit>> {
        return dao.getByTypeId(typeId)
    }

    fun observeAll(): Flow<List<MeasurementTypeUnit>> {
        return dao.observeAll()
    }

    fun update(typeUnit: MeasurementTypeUnit) {
        dao.update(typeUnit)
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}