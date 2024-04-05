package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.DeleteModal
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class MeasurementTypeFormViewModel(
    private val type: MeasurementType,
    getMeasurementTypeUseCase: GetMeasurementTypeUseCase = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
    private val deleteMeasurementType: DeleteMeasurementTypeUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<MeasurementTypeFormViewState, MeasurementTypeFormIntent>() {

    var typeName = MutableStateFlow(type.name)
    var unitName = MutableStateFlow(type.selectedUnit.name)

    var valueRangeMinimum = MutableStateFlow(type.range.minimum.toString())
    var valueRangeLow = MutableStateFlow(type.range.low?.toString() ?: "")
    var valueRangeTarget = MutableStateFlow(type.range.target?.toString() ?: "")
    var valueRangeHigh = MutableStateFlow(type.range.high?.toString() ?: "")
    var valueRangeMaximum = MutableStateFlow(type.range.maximum.toString())
    var isValueRangeHighlighted = MutableStateFlow(type.range.isHighlighted)

    override val state = combine(
        getMeasurementTypeUseCase(type),
        unitName,
    ) { type, unitName ->
        when (type) {
            null -> MeasurementTypeFormViewState.Error
            else ->  MeasurementTypeFormViewState.Loaded(
                type = type,
                unitName = if (type.isUserGenerated) unitName else type.selectedUnit.abbreviation,
            )
        }
    }

    override fun handleIntent(intent: MeasurementTypeFormIntent) {
        when (intent) {
            is MeasurementTypeFormIntent.EditTypeName -> typeName.value = intent.input
            is MeasurementTypeFormIntent.EditUnitName -> unitName.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeMinimum -> valueRangeMinimum.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeLow -> valueRangeLow.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeTarget -> valueRangeTarget.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeHigh -> valueRangeHigh.value = intent.input
            is MeasurementTypeFormIntent.EditValueRangeMaximum -> valueRangeMaximum.value = intent.input
            is MeasurementTypeFormIntent.EditIsValueRangeHighlighted -> isValueRangeHighlighted.value = intent.input
            is MeasurementTypeFormIntent.UpdateType -> updateType()
            is MeasurementTypeFormIntent.DeleteType -> deleteType()
        }
    }

    private fun updateType() {
        // TODO: Update unit name
        updateMeasurementType(
            type.copy(
                name = typeName.value,
                range = MeasurementValueRange(
                    minimum = valueRangeMinimum.value.toDouble(),
                    low = valueRangeLow.value.toDoubleOrNull(),
                    target = valueRangeLow.value.toDoubleOrNull(),
                    high = valueRangeHigh.value.toDoubleOrNull(),
                    maximum = valueRangeMaximum.value.toDouble(),
                    isHighlighted = isValueRangeHighlighted.value,
                )
            )
        )
        navigateBack()
    }

    private fun deleteType() {
        openModal(
            DeleteModal(
                onDismissRequest = closeModal::invoke,
                onConfirm = {
                    deleteMeasurementType(type)
                    closeModal()
                    navigateBack()
                }
            )
        )
    }
}