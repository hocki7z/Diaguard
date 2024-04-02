package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRange
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class MeasurementTypeRepository(
    private val dao: MeasurementTypeDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        key: String?,
        name: String,
        range: MeasurementValueRange,
        sortIndex: Long,
        propertyId: Long,
    ): Long {
        dao.create(
            createdAt = dateTimeFactory.now(),
            key = key,
            name = name,
            range = range,
            sortIndex = sortIndex,
            // We set this temporary id because the corresponding unit will be created afterwards
            selectedUnitId = MeasurementType.SELECTED_UNIT_ID_INVALID,
            propertyId = propertyId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementType? {
        return dao.getById(id)
    }

    fun observeById(id: Long): Flow<MeasurementType?> {
        return dao.observeById(id)
    }

    fun getByKey(key: String): MeasurementType {
        return checkNotNull(dao.getByKey(key))
    }

    fun getByPropertyId(propertyId: Long): List<MeasurementType> {
        return dao.getByPropertyId(propertyId)
    }

    fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementType>> {
        return dao.observeByPropertyId(propertyId)
    }

    fun getAll(): List<MeasurementType> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<MeasurementType>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        name: String,
        range: MeasurementValueRange,
        sortIndex: Long,
        selectedUnitId: Long,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            range = range,
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}

fun Flow<MeasurementType>.deep(
    propertyRepository: MeasurementPropertyRepository = inject(),
): Flow<MeasurementType> {
    return flatMapLatest { type ->
        val flows = propertyRepository.observeById(type.propertyId)
            .filterNotNull()
            .map { property -> type to property }
        combine(flows) { typeWithProperty ->
            typeWithProperty.map { (type, property) ->
                // TODO: Set selected unit
                type.property = property
                type
            }.first()
        }
    }
}

fun MeasurementType.deep(
    propertyRepository: MeasurementPropertyRepository = inject(),
): MeasurementType {
    return apply {
        this.property = checkNotNull(propertyRepository.getById(propertyId))
        // TODO: Selected unit
    }
}