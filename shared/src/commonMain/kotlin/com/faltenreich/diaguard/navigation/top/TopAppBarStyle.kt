package com.faltenreich.diaguard.navigation.top

import androidx.compose.runtime.Composable

sealed class TopAppBarStyle {

    data object Hidden : TopAppBarStyle()

    data class CenterAligned(val content: @Composable () -> Unit) : TopAppBarStyle()
}