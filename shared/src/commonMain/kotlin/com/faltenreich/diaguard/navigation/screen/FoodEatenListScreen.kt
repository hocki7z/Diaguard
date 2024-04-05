package com.faltenreich.diaguard.navigation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.list.FoodEatenList
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListIntent
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

data class FoodEatenListScreen(val food: Food) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(getString(Res.string.food_eaten))
                Text(
                    text = food.name,
                    style = AppTheme.typography.bodySmall,
                )
            }
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<FoodEatenListViewModel>()
                FloatingActionButton(onClick = { viewModel.dispatchIntent(FoodEatenListIntent.CreateEntry(food)) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.entry_new_description),
                    )
                }
            },
        )

    @Composable
    override fun Content() {
        FoodEatenList(viewModel = getViewModel { parametersOf(food) })
    }
}