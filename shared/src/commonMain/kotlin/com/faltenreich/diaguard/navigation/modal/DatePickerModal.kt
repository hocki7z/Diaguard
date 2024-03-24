package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.datetime.DatePicker

data class DatePickerModal(
    private val date: Date,
    private val onPick: (Date) -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        DatePicker(date, onPick)
    }
}