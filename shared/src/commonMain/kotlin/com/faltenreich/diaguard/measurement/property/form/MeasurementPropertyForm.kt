package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormDialog
import com.faltenreich.diaguard.measurement.type.list.MeasurementTypeList
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.EmojiPicker
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun MeasurementPropertyForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyFormViewModel = inject(),
) {
    val navigator = LocalNavigator.currentOrThrow

    when (val viewState = viewModel.viewState.collectAsState().value) {
        is MeasurementPropertyFormViewState.Loading -> LoadingIndicator(modifier = modifier)

        is MeasurementPropertyFormViewState.Loaded -> {
            LazyColumn(modifier = modifier) {
                item {
                    Column(
                        modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
                    ) {
                        TextInput(
                            input = viewModel.name.collectAsState().value,
                            onInputChange = { input -> viewModel.name.value = input },
                            label = getString(MR.strings.name),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        TextInput(
                            input = viewModel.icon.collectAsState().value,
                            onInputChange = { input -> viewModel.icon.value = input },
                            label = getString(MR.strings.icon),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        EmojiPicker(onEmojiPicked = { emoji -> viewModel.icon.value = emoji })
                    }
                }

                if (viewState.property.isPredefined && viewState.types.size == 1) {
                    MeasurementUnitList(units = viewState.types.first().units)
                } else {
                    MeasurementTypeList(types = viewState.types)
                }
            }

            if (viewState.showFormDialog) {
                MeasurementTypeFormDialog(
                    onDismissRequest = viewModel::hideFormDialog,
                    onConfirmRequest = { typeName, unitName ->
                        viewModel.createType(
                            typeName = typeName,
                            unitName = unitName,
                            types = viewState.types,
                        )
                        viewModel.hideFormDialog()
                    }
                )
            }

            if (viewState.showDeletionDialog) {
                AlertDialog(
                    onDismissRequest = viewModel::hideDeletionDialog,
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteProperty(viewState.property)
                            viewModel.hideDeletionDialog()
                            navigator.pop()
                        }) {
                            Text(getString(MR.strings.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::hideDeletionDialog) {
                            Text(getString(MR.strings.cancel))
                        }
                    },
                    title = { Text(getString(MR.strings.measurement_property_delete)) },
                    text = {
                        Text(
                            getString(
                                MR.strings.measurement_property_delete_description,
                                viewState.measurementCount
                            )
                        )
                    },
                )
            }
        }
    }
}