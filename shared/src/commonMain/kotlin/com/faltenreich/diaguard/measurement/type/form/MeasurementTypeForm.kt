package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.FormRowLabel
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.TextInput
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementTypeForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementTypeFormViewModel = inject(),
) {
    val navigator = LocalNavigator.currentOrThrow

    when (val state = viewModel.viewState.collectAsState().value) {
        is MeasurementTypeFormViewState.Loading -> LoadingIndicator()

        is MeasurementTypeFormViewState.Loaded -> {
            Column(modifier = modifier) {
                TextInput(
                    input = viewModel.typeName.collectAsState().value,
                    onInputChange = { input -> viewModel.typeName.value = input },
                    label = stringResource(MR.strings.name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = AppTheme.dimensions.padding.P_3),
                )
                if (state.type.property.isUserGenerated) {
                    TextInput(
                        input = viewModel.unitName.collectAsState().value,
                        onInputChange = { input -> viewModel.unitName.value = input },
                        label = stringResource(MR.strings.measurement_unit),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.dimensions.padding.P_3),
                    )
                } else {
                    FormRowLabel(stringResource(MR.strings.measurement_units))
                    MeasurementUnitList(
                        units = state.type.units,
                        onItemClick = viewModel::setSelectedUnit,
                    )
                }
            }

            if (state.showDeletionDialog) {
                AlertDialog(
                    onDismissRequest = viewModel::hideDeletionDialog,
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteType(state.type)
                            viewModel.hideDeletionDialog()
                            navigator.pop()
                        }) {
                            Text(stringResource( MR.strings.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::hideDeletionDialog) {
                            Text(stringResource( MR.strings.cancel))
                        }
                    },
                    title = { Text(stringResource(MR.strings.measurement_type_delete)) },
                    text = { Text(stringResource(MR.strings.measurement_type_delete_description, state.measurementCount)) },
                )
            }
        }

        is MeasurementTypeFormViewState.Error -> Unit
    }
}