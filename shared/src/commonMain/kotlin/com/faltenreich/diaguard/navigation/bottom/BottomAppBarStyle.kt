package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.runtime.Composable

sealed class BottomAppBarStyle {

    data object Hidden : BottomAppBarStyle()

    data class Visible(
        val actions: @Composable () -> Unit = {},
        val floatingActionButton: @Composable () -> Unit = {},
    ) : BottomAppBarStyle()
}