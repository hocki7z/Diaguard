package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormDialog
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MeasurementPropertyList(
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyListViewModel = inject(),
) {
    val state = viewModel.collectState()
    val items = (state as? MeasurementPropertyListViewState.Loaded)?.items ?: emptyList()

    AnimatedVisibility(
        visible = items.isNotEmpty(),
        enter = fadeIn(),
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            items.forEachIndexed { index, item ->
                MeasurementPropertyListItem(
                    property = item,
                    onArrowUp = {
                        viewModel.dispatchIntent(
                            MeasurementPropertyListIntent.DecrementSortIndex(
                                item
                            )
                        )
                    },
                    showArrowUp = index > 0,
                    onArrowDown = {
                        viewModel.dispatchIntent(
                            MeasurementPropertyListIntent.IncrementSortIndex(
                                item
                            )
                        )
                    },
                    showArrowDown = index < items.size - 1,
                    modifier = Modifier.clickable {
                        viewModel.dispatchIntent(
                            MeasurementPropertyListIntent.EditProperty(
                                item
                            )
                        )
                    },
                )
            }
        }
    }

    // TODO: Replace with Modal
    if (false) {
        MeasurementPropertyFormDialog(
            onDismissRequest = { viewModel.dispatchIntent(MeasurementPropertyListIntent.HideFormDialog) },
            onConfirmRequest = { name ->
                viewModel.dispatchIntent(MeasurementPropertyListIntent.CreateProperty(name, items))
                viewModel.dispatchIntent(MeasurementPropertyListIntent.HideFormDialog)
            }
        )
    }
}