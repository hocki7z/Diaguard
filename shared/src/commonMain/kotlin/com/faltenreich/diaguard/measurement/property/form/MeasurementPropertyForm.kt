package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.measurement.type.list.MeasurementTypeList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.EmojiPicker
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.rememberBottomSheetState

@Composable
fun MeasurementPropertyForm(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyFormViewModel = inject(),
) {
    when (val viewState = viewModel.collectState()) {
        null -> LoadingIndicator(modifier = modifier)

        is MeasurementPropertyFormViewState.Loaded -> {
            Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                TextInput(
                    input = viewModel.name.collectAsState().value,
                    onInputChange = { input -> viewModel.name.value = input },
                    label = getString(MR.strings.name),
                    leadingIcon = {
                        IconButton(onClick = {
                            viewModel.dispatchIntent(MeasurementPropertyFormIntent.ShowIconPicker)
                        }) {
                            MeasurementPropertyIcon(text = viewModel.icon.collectAsState().value)
                        }
                    },
                    modifier = Modifier
                        .padding(all = AppTheme.dimensions.padding.P_3)
                        .fillMaxWidth(),
                )
                MeasurementTypeList(
                    property = viewModel.property,
                    types = viewState.types,
                )
            }

            if (viewState.showIconPicker) {
                BottomSheet(
                    onDismissRequest = {
                        viewModel.dispatchIntent(MeasurementPropertyFormIntent.HideIconPicker)
                    },
                    sheetState = rememberBottomSheetState(),
                ) {
                    EmojiPicker(onEmojiPicked = { emoji -> viewModel.icon.value = emoji })
                }
            }

            if (viewState.showDeletionDialog) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.dispatchIntent(MeasurementPropertyFormIntent.HideDeletionDialog)
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.dispatchIntent(MeasurementPropertyFormIntent.DeleteProperty)
                        }) {
                            Text(getString(MR.strings.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            viewModel.dispatchIntent(MeasurementPropertyFormIntent.HideDeletionDialog)
                        }) {
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