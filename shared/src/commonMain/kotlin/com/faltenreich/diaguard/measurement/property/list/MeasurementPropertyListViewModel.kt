package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.MeasurementPropertyFormModal
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class MeasurementPropertyListViewModel(
    getMeasurementProperties: GetMeasurementPropertiesUseCase = inject(),
    private val updateMeasurementProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val createMeasurementProperty: CreateMeasurementPropertyUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<MeasurementPropertyListViewState, MeasurementPropertyListIntent>() {

    override val state = getMeasurementProperties().map(MeasurementPropertyListViewState::Loaded)

    private val properties: List<MeasurementProperty>?
        get() = (stateInScope.value as? MeasurementPropertyListViewState.Loaded)?.items

    override fun handleIntent(intent: MeasurementPropertyListIntent) {
        when (intent) {
            is MeasurementPropertyListIntent.DecrementSortIndex -> decrementSortIndex(intent.property)
            is MeasurementPropertyListIntent.IncrementSortIndex -> incrementSortIndex(intent.property)
            is MeasurementPropertyListIntent.Edit -> editProperty(intent.property)
            is MeasurementPropertyListIntent.Create -> createProperty()
        }
    }

    private fun decrementSortIndex(property: MeasurementProperty) {
        val properties = properties ?: return
        swapSortIndexes(first = property, second = properties.last { it.sortIndex < property.sortIndex })
    }

    private fun incrementSortIndex(property: MeasurementProperty) {
        val properties = properties ?: return
        swapSortIndexes(first = property, second = properties.first { it.sortIndex > property.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementProperty,
        second: MeasurementProperty,
    ) {
        updateMeasurementProperty(first.copy(sortIndex = second.sortIndex))
        updateMeasurementProperty(second.copy(sortIndex = first.sortIndex))
    }

    private fun editProperty(property: MeasurementProperty) {
        navigateToScreen(MeasurementPropertyFormScreen(property))
    }

    private fun createProperty() {
        val properties = properties ?: return
        openModal(
            MeasurementPropertyFormModal(
                onDismissRequest = closeModal::invoke,
                onConfirmRequest = { name ->
                    createMeasurementProperty(
                        name = name,
                        key = null,
                        icon = null,
                        sortIndex = properties.maxOf(MeasurementProperty::sortIndex) + 1,
                    )
                    closeModal()
                }
            )
        )
    }
}