package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PreferenceListViewModel(
    preferences: List<PreferenceListItem>?,
    getDefaultPreferences: GetDefaultPreferencesUseCase = inject(),
) : ViewModel<PreferenceListState, Unit, Unit>() {

    override val state = (preferences?.let(::flowOf)
        ?: getDefaultPreferences()).map(::PreferenceListState)
}