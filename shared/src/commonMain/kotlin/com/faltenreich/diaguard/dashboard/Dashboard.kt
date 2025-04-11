package com.faltenreich.diaguard.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.average.AverageDashboardItem
import com.faltenreich.diaguard.dashboard.hba1c.HbA1cDashboardItem
import com.faltenreich.diaguard.dashboard.latest.LatestDashboardItem
import com.faltenreich.diaguard.dashboard.today.TodayDashboardItem
import com.faltenreich.diaguard.dashboard.trend.TrendDashboardItem
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.app_name

@Composable
fun Dashboard(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(getString(Res.string.app_name)) }) },
    ) {
        when (val state = viewModel.collectState()) {
            null -> Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            else -> Column(
                modifier = modifier
                    .padding(all = AppTheme.dimensions.padding.P_3)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            ) {
                LatestDashboardItem(
                    data = state.latestBloodSugar,
                    onClick = { entry ->
                        val intent = entry?.let(DashboardIntent::EditEntry) ?: DashboardIntent.CreateEntry
                        viewModel.dispatchIntent(intent)
                    },
                    modifier = modifier.fillMaxWidth(),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
                ) {
                    TodayDashboardItem(
                        data = state.today,
                        onClick = { viewModel.dispatchIntent(DashboardIntent.OpenStatistic) },
                        modifier = Modifier.weight(1f),
                    )
                    AverageDashboardItem(
                        data = state.average,
                        onClick = { viewModel.dispatchIntent(DashboardIntent.OpenStatistic) },
                        modifier = Modifier.weight(1f),
                    )
                }
                HbA1cDashboardItem(
                    data = state.hbA1c,
                    modifier = Modifier.fillMaxWidth(),
                )
                TrendDashboardItem(
                    data = state.trend,
                    onClick = { viewModel.dispatchIntent(DashboardIntent.OpenStatistic) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}