package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.faltenreich.diaguard.shared.view.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.LoadingIndicator

@Composable
fun EntrySearch(
    modifier: Modifier = Modifier,
    viewModel: EntrySearchViewModel = inject(),
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val viewState = viewModel.collectState()) {
            null -> Unit
            is EntrySearchViewState.Idle ->  Text(getString(Res.string.entry_search_placeholder))
            is EntrySearchViewState.Loading -> LoadingIndicator()
            is EntrySearchViewState.Result -> LazyColumn(modifier = modifier) {
                items(items = viewState.items ?: emptyList(), key = Entry.Local::id) { entry ->
                    Column(
                        modifier = Modifier.animateItemPlacement(),
                    ) {
                        EntryListItem(
                            entry = entry,
                            onClick = { viewModel.dispatchIntent(EntrySearchIntent.OpenEntry(entry)) },
                        )
                        Divider()
                    }
                }
            }
        }
    }
}