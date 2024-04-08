package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.shared.di.inject

class UpdateMeasurementCategoryUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository = inject(),
) {

    operator fun invoke(category: MeasurementCategory) {
        measurementCategoryRepository.update(category)
    }
}