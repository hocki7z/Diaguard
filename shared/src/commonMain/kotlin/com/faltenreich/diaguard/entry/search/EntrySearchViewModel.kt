package com.faltenreich.diaguard.entry.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

class EntrySearchViewModel(
    query: String = "",
    searchEntries: SearchEntriesUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<EntrySearchState, EntrySearchIntent, Unit>() {

    override val state = MutableStateFlow<EntrySearchState>(EntrySearchState.Idle)

    var query: String by mutableStateOf("")

    init {
        snapshotFlow { this.query }
            .debounce(1.seconds)
            .distinctUntilChanged()
            .onEach { state.value = EntrySearchState.Loading }
            .flatMapLatest { searchEntries(it) }
            .onEach {
                state.value =
                    if (this.query.isBlank()) EntrySearchState.Idle
                    else EntrySearchState.Result(it)
            }
            .launchIn(scope)

        if (query.isNotBlank()) {
            this.query = query
        }
    }

    override suspend fun handleIntent(intent: EntrySearchIntent) {
        when (intent) {
            is EntrySearchIntent.OpenEntry -> navigateToScreen(EntryFormScreen(entry = intent.entry))
        }
    }
}