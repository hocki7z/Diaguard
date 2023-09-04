package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMeasurementTypesUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(property: MeasurementProperty): Flow<List<MeasurementType>> {
        return combine(
            measurementTypeRepository.observeByPropertyId(property.id),
            measurementUnitRepository.observeByPropertyId(property.id),
        ) { types, units ->
            types.onEach { type ->
                type.units = units
                    .filter { unit -> unit.typeId == type.id }
                    .onEach { unit -> unit.type = type }
            }
        }
    }
}