package com.faltenreich.diaguard.navigation.bar.top

import androidx.compose.runtime.Composable

sealed interface TopAppBarStyle {

    data object Hidden : TopAppBarStyle

    data class CenterAligned(val content: @Composable () -> Unit) : TopAppBarStyle

    data class Custom(val content: @Composable () -> Unit) : TopAppBarStyle
}