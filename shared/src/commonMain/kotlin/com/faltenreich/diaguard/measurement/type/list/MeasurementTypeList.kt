package com.faltenreich.diaguard.measurement.type.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormDialog
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.TextDivider
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeasurementTypeList(
    property: MeasurementProperty,
    types: List<MeasurementType>,
    modifier: Modifier = Modifier,
    viewModel: MeasurementTypeListViewModel = inject(),
) {
    when (val viewState = viewModel.collectState()) {
        null -> LoadingIndicator(modifier = modifier)

        else -> {
            Column(modifier = modifier) {
                TextDivider(getString(Res.string.measurement_types))

                types.forEachIndexed { index, type ->
                    MeasurementTypeListItem(
                        type = type,
                        onArrowUp = {
                            viewModel.dispatchIntent(
                                MeasurementTypeListIntent.DecrementSortIndex(type, types)
                            )
                        },
                        showArrowUp = index > 0,
                        onArrowDown = {
                            viewModel.dispatchIntent(
                                MeasurementTypeListIntent.IncrementSortIndex(type, types)
                            )
                        },
                        showArrowDown = index < types.size - 1,
                        modifier = Modifier.clickable {
                            viewModel.dispatchIntent(
                                MeasurementTypeListIntent.EditType(type)
                            )
                        },
                    )
                    Divider()
                }

                FormRow {
                    SuggestionChip(
                        onClick = { viewModel.dispatchIntent(MeasurementTypeListIntent.ShowFormDialog) },
                        label = { Text(stringResource(Res.string.measurement_type_add)) },
                    )
                }
            }

            if (viewState.showFormDialog) {
                MeasurementTypeFormDialog(
                    onDismissRequest = {
                        viewModel.dispatchIntent(MeasurementTypeListIntent.HideFormDialog)
                    },
                    onConfirmRequest = { typeName, unitName ->
                        viewModel.dispatchIntent(
                            MeasurementTypeListIntent.CreateType(
                                typeName = typeName,
                                unitName = unitName,
                                types = types,
                                propertyId = property.id,
                            )
                        )
                        viewModel.dispatchIntent(MeasurementTypeListIntent.HideFormDialog)
                    }
                )
            }
        }
    }
}