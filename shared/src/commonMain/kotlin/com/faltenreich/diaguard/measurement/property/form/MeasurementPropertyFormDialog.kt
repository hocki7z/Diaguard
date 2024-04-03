package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.rememberFocusRequester

@Composable
fun MeasurementPropertyFormDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (propertyName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = rememberFocusRequester(requestFocus = true)

    var propertyName by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { onConfirmRequest(propertyName) }) {
                Text(getString(MR.strings.create))
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(getString(MR.strings.cancel))
            }
        },
        title = { Text(getString(MR.strings.measurement_property_new)) },
        text = {
            TextInput(
                input = propertyName,
                onInputChange = { propertyName = it },
                label = getString(MR.strings.name),
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            )
        }
    )
}