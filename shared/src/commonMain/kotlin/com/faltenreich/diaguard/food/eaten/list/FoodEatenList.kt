package com.faltenreich.diaguard.food.eaten.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.faltenreich.diaguard.shared.view.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun FoodEatenList(
    modifier: Modifier = Modifier,
    viewModel: FoodEatenListViewModel,
) {
    when (val viewState = viewModel.collectState()) {
        null -> Unit
        is FoodEatenListViewState.Empty -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(getString(Res.string.no_entries))
        }
        is FoodEatenListViewState.Loaded -> Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
        ) {
            viewState.results.forEach { foodEaten ->
                FoodEatenListItem(
                    foodEaten = foodEaten,
                    onIntent = viewModel::dispatchIntent,
                )
                Divider()
            }
        }
    }
}