package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.GetAverageUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetCurrentHbA1cUseCase
import com.faltenreich.diaguard.dashboard.latest.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.today.GetTodayUseCase
import com.faltenreich.diaguard.dashboard.trend.GetTrendUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.statistic.StatisticScreen
import kotlinx.coroutines.flow.combine

class DashboardViewModel(
    getLatestBloodSugar: GetLatestBloodSugarUseCase,
    getToday: GetTodayUseCase,
    getAverage: GetAverageUseCase,
    getCurrentHbA1c: GetCurrentHbA1cUseCase,
    getTrend: GetTrendUseCase,
    private val pushScreen: PushScreenUseCase,
) : ViewModel<DashboardState, DashboardIntent, Unit>() {

    override val state = combine(
        getLatestBloodSugar(),
        getToday(),
        getAverage(),
        getCurrentHbA1c(),
        getTrend(),
        ::DashboardState,
    )

    override suspend fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.CreateEntry -> pushScreen(EntryFormScreen())
            is DashboardIntent.EditEntry -> pushScreen(EntryFormScreen(entry = intent.entry))
            is DashboardIntent.OpenStatistic -> pushScreen(StatisticScreen)
            is DashboardIntent.SearchEntries -> pushScreen(EntrySearchScreen())
        }
    }
}