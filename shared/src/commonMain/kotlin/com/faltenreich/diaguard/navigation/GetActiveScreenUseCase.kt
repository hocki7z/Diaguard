package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen

class GetActiveScreenUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(): Screen? {
        return navigation.lastItem
    }
}