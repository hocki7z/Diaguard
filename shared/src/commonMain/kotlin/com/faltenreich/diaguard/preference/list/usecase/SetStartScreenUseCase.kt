package com.faltenreich.diaguard.preference.list.usecase

import com.faltenreich.diaguard.preference.PreferenceStore
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.shared.di.inject

class SetStartScreenUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    suspend operator fun invoke(startScreen: StartScreen) {
        preferenceStore.setStartScreen(startScreen)
    }
}