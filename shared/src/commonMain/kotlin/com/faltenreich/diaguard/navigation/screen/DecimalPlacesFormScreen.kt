package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesForm

data object DecimalPlacesFormScreen : Screen {

    @Composable
    override fun Content() {
        DecimalPlacesForm()
    }
}