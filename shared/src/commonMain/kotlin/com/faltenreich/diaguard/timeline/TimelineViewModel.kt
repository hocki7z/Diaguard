package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.datetime.GetTodayUseCase
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TimelineViewModel(
    date: Date?,
    valueRepository: MeasurementValueRepository = inject(),
    measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    getToday: GetTodayUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<TimelineState, TimelineIntent>() {

    private val initialDate = date ?: getToday()
    private val currentDate = MutableStateFlow(initialDate)
    private val values = currentDate.flatMapLatest { date ->
        valueRepository.observeByDateRange(
            startDateTime = date.minus(2, DateUnit.MONTH).atStartOfDay(),
            endDateTime = date.plus(2, DateUnit.DAY).atEndOfDay(),
        )
    }
    private val valuesForChart = values.map { it.filter { value -> value.type.property.isBloodSugar } }
    private val valuesForList = values.map { it.filterNot { value -> value.type.property.isBloodSugar } }
    private val propertiesForList = measurementPropertyRepository.observeAll().map { properties ->
        properties.filterNot(MeasurementProperty::isBloodSugar)
    }

    override val state = combine(
        flowOf(initialDate),
        currentDate,
        valuesForChart,
        valuesForList,
        propertiesForList,
        ::TimelineState,
    )

    override fun onIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.CreateEntry -> navigateToScreen(EntryFormScreen())
            is TimelineIntent.SearchEntries -> navigateToScreen(EntrySearchScreen())
            is TimelineIntent.SetDate -> currentDate.value = intent.date
        }
    }
}