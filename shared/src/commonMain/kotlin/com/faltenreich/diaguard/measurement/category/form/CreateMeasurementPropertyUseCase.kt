package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange

class CreateMeasurementPropertyUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val unitRepository: MeasurementUnitRepository,
) {

    operator fun invoke(
        propertyName: String,
        propertySortIndex: Long,
        propertyAggregationStyle: MeasurementAggregationStyle,
        propertyRange: MeasurementValueRange,
        category: MeasurementCategory.Local,
        unitName: String,
        unitIsSelected: Boolean,
    ): MeasurementProperty.Local {
        val unitFactor = MeasurementUnit.FACTOR_DEFAULT
        // TODO: Make customizable?
        val unitAbbreviation = unitName

        val propertyId = propertyRepository.create(
            MeasurementProperty.User(
                name = propertyName,
                sortIndex = propertySortIndex,
                aggregationStyle = propertyAggregationStyle,
                range = propertyRange,
                category = category,
            )
        )

        val property = checkNotNull(propertyRepository.getById(propertyId))

        val unit = MeasurementUnit.User(
            name = unitName,
            abbreviation = unitAbbreviation,
            factor = unitFactor,
            isSelected = unitIsSelected,
            property = property,
        )

        unitRepository.create(unit)

        return checkNotNull(propertyRepository.getById(propertyId))
    }
}