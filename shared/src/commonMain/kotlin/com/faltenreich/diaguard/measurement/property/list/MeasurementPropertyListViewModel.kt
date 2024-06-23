package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.category.form.CreateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormModal
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.flowOf

class MeasurementPropertyListViewModel(
    private val createProperty: CreateMeasurementPropertyUseCase = inject(),
    private val updateProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<Unit, MeasurementPropertyListIntent, Unit>() {

    override val state = flowOf(Unit)

    override suspend fun handleIntent(intent: MeasurementPropertyListIntent) {
        when (intent) {
            is MeasurementPropertyListIntent.DecrementSortIndex -> decrementSortIndex(intent)
            is MeasurementPropertyListIntent.IncrementSortIndex -> incrementSortIndex(intent)
            is MeasurementPropertyListIntent.EditProperty -> editProperty(intent)
            is MeasurementPropertyListIntent.CreateProperty -> createProperty(intent)
        }
    }

    private fun decrementSortIndex(intent: MeasurementPropertyListIntent.DecrementSortIndex) = with(intent) {
        swapSortIndexes(first = property, second = inProperties.last { it.sortIndex < property.sortIndex })
    }

    private fun incrementSortIndex(intent: MeasurementPropertyListIntent.IncrementSortIndex) = with(intent) {
        swapSortIndexes(first = property, second = inProperties.first { it.sortIndex > property.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementProperty.Local,
        second: MeasurementProperty.Local,
    ) {
        updateProperty(first.copy(sortIndex = second.sortIndex))
        updateProperty(second.copy(sortIndex = first.sortIndex))
    }

    private fun editProperty(intent: MeasurementPropertyListIntent.EditProperty) = with(intent) {
        navigateToScreen(MeasurementPropertyFormScreen(property))
    }

    private fun createProperty(intent: MeasurementPropertyListIntent.CreateProperty) = with(intent) {
        openModal(
            MeasurementPropertyFormModal(
                onDismissRequest = closeModal::invoke,
                onConfirmRequest = { propertyName, unitName ->
                    val property = createProperty(
                        propertyName = propertyName,
                        propertySortIndex = properties.maxOfOrNull(MeasurementProperty::sortIndex)?.plus(1) ?: 0,
                        // TODO: Make user-customizable
                        propertyAggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                        // TODO: Make user-customizable
                        propertyRange = MeasurementValueRange(
                            minimum = 0.0,
                            low = null,
                            target = null,
                            high = null,
                            maximum = Double.MAX_VALUE,
                            isHighlighted = false,
                        ),
                        category = category,
                        unitName = unitName,
                        unitIsSelected = true,
                    )
                    closeModal()
                    navigateToScreen(MeasurementPropertyFormScreen(property))
                }
            )
        )
    }
}