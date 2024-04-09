package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class CountMeasurementValuesOfPropertyUseCase(
    private val measurementValueRepository: MeasurementValueRepository = inject(),
) {

    operator fun invoke(measurementProperty: MeasurementProperty): Flow<Long> {
        return measurementValueRepository.observeCountByPropertyId(measurementProperty.id)
    }
}