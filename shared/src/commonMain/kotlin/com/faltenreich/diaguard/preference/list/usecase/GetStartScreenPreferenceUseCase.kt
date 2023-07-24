package com.faltenreich.diaguard.preference.list.usecase

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.PreferenceStore
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.item.SelectablePreferenceOption
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStartScreenPreferenceUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    operator fun invoke(): Flow<Preference> {
        return preferenceStore.startScreen.map { startScreen ->
            Preference.Selection(
                title = { stringResource(MR.strings.start_screen) },
                subtitle = { stringResource(startScreen.labelResource) },
                options = StartScreen.values().map { value ->
                    SelectablePreferenceOption(
                        label = { stringResource(value.labelResource) },
                        isSelected = value == startScreen,
                        onSelected = { preferenceStore.setStartScreen(value) },
                    )
                },
            )
        }
    }
}