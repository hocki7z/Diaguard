package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.sqldelight.GetAverageByDateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject

class MeasurementValueSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val typeMapper: MeasurementTypeSqlDelightMapper = inject(),
    private val unitMapper: MeasurementUnitSqlDelightMapper = inject(),
    private val propertyMapper: MeasurementPropertySqlDelightMapper = inject(),
    private val entryMapper: EntrySqlDelightMapper = inject(),
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        value: Double,
        typeId: Long,
        entryId: Long,
    ): MeasurementValue {
        return MeasurementValue(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            value = value,
            typeId = typeId,
            entryId = entryId,
        )
    }

    fun map(
        valueId: Long,
        valueCreatedAt: String,
        valueUpdatedAt: String,
        valueValue: Double,
        valueTypeId: Long,
        valueEntryId: Long,
        typeId: Long,
        typeCreatedAt: String,
        typeUpdatedAt: String,
        typeKey: String?,
        typeName: String,
        typeSortIndex: Long,
        typeSelectedUnitId: Long?,
        typePropertyId: Long,
        unitId: Long,
        unitCreatedAt: String,
        unitUpdatedAt: String,
        unitName: String,
        unitFactor: Double,
        unitTypeId: Long,
        propertyId: Long,
        propertyCreatedAt: String,
        propertyUpdatedAt: String,
        propertyKey: String?,
        propertyName: String,
        propertyIcon: String?,
        propertySortIndex: Long,
        entryId: Long,
        entryCreatedAt: String,
        entryUpdatedAt: String,
        entryDateTime: String,
        entryNote: String?,
    ): MeasurementValue {
        return MeasurementValue(
            id = valueId,
            createdAt = dateTimeFactory.dateTime(isoString = valueCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = valueUpdatedAt),
            value = valueValue,
            typeId = valueTypeId,
            entryId = valueEntryId,
        ).apply {
            type = typeMapper.map(
                id = typeId,
                createdAt = typeCreatedAt,
                updatedAt = typeUpdatedAt,
                key = typeKey,
                name = typeName,
                sortIndex = typeSortIndex,
                selectedUnitId = typeSelectedUnitId,
                propertyId = typePropertyId,
            ).apply {
                property = propertyMapper.map(
                    id = propertyId,
                    createdAt = propertyCreatedAt,
                    updatedAt = propertyUpdatedAt,
                    key = propertyKey,
                    name = propertyName,
                    icon = propertyIcon,
                    sortIndex = propertySortIndex,
                )
                selectedUnit = unitMapper.map(
                    id = unitId,
                    createdAt = unitCreatedAt,
                    updatedAt = unitUpdatedAt,
                    name = unitName,
                    factor = unitFactor,
                    typeId = unitTypeId,
                )
            }
            entry = entryMapper.map(
                id = entryId,
                createdAt = entryCreatedAt,
                updatedAt = entryUpdatedAt,
                dateTime = entryDateTime,
                note = entryNote,
            )
        }
    }

    fun map(averageByDateTime: GetAverageByDateTime): Double? {
        return averageByDateTime.AVG
    }
}