package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.form.measurement.MeasurementInputViewState
import com.faltenreich.diaguard.shared.datetime.DateTime

data class EntryFormViewState(
    val dateTime: DateTime,
    val note: String?,
    val isEditing: Boolean,
    val measurementInputViewState: MeasurementInputViewState,
)