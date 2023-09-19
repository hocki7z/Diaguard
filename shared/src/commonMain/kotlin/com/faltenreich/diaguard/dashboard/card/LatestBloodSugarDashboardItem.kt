package com.faltenreich.diaguard.dashboard.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.navigation.Screen
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun LatestDashboardItem(
    data: DashboardViewState.Revisit.LatestBloodSugar?,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        onClick = { navigator.push(Screen.EntryForm(entry = data?.entry)) },
        modifier = modifier,
    ) {
        when {
            data != null -> Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = AppTheme.dimensions.padding.P_3)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = data.value,
                        style = AppTheme.typography.displayLarge,
                    )
                    Text(data.timePassed)
                }
            }
            else -> Text(
                text = stringResource(MR.strings.entry_first_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = AppTheme.dimensions.padding.P_3),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.headlineMedium,
            )
        }
    }
}