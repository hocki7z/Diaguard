package com.faltenreich.diaguard.preference.decimal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomSheetContainer
import com.faltenreich.diaguard.shared.di.getViewModel

data object DecimalPlacesFormScreen : Screen {

    @Composable
    override fun Content() {
        BottomSheetContainer {
            DecimalPlacesForm(viewModel = getViewModel())
        }
    }
}