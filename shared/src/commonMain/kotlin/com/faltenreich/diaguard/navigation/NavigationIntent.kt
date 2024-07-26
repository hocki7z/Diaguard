package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface NavigationIntent {

    data class NavigateTo(val screen: Screen, val clearBackStack: Boolean) : NavigationIntent

    data object NavigateBack : NavigationIntent

    data class OpenBottomSheet(val screen: Screen) : NavigationIntent

    data object CloseBottomSheet : NavigationIntent
}