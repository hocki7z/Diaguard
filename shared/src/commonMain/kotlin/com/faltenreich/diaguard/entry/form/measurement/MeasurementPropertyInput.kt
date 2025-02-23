package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun MeasurementPropertyInput(
    data: MeasurementPropertyInputState,
    action: @Composable (() -> Unit)? = null,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var input by remember { mutableStateOf(data.input) }

    TextInput(
        input = input,
        onInputChange = {
            input = it
            onIntent(EntryFormIntent.Edit(data.copy(input = input)))
        },
        modifier = modifier,
        placeholder = { Text(data.property.selectedUnit.abbreviation) },
        trailingIcon = action,
        suffix = {
            if (data.property.name != data.property.category.name) {
                Text(data.property.name)
            }
        },
        supportingText = data.error?.let { error -> { Text(error) } },
        isError = data.error != null,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = if (data.isLast) ImeAction.Done else ImeAction.Next,
        ),
    )
}