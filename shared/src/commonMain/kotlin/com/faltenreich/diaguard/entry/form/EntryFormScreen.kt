package com.faltenreich.diaguard.entry.form

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry
import diaguard.shared.generated.resources.entry_delete
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.save
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class EntryFormScreen(
    val entryId: Long,
    val dateTimeIsoString: String?,
    val foodId: Long,
) : Screen {

    constructor(
        entry: Entry.Local? = null,
        date: Date? = null,
        food: Food.Local? = null,
    ) : this(
        entryId = entry?.id ?: -1,
        dateTimeIsoString = date?.atStartOfDay()?.isoString,
        foodId = food?.id ?: -1,
    )

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.entry))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = viewModel<EntryFormViewModel>()
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.entry_delete,
                    onClick = { viewModel.dispatchIntent(EntryFormIntent.Delete) },
                )
            },
            floatingActionButton = {
                val viewModel = viewModel<EntryFormViewModel>()
                FloatingActionButton(onClick = { viewModel.dispatchIntent(EntryFormIntent.Submit) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.save),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        EntryForm(
            viewModel = koinViewModel(parameters = { parametersOf(entryId, dateTimeIsoString, foodId) }),
            foodSearchViewModel = koinViewModel(parameters = { parametersOf(FoodSearchMode.FIND) }),
        )
    }
}