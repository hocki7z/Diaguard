package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.datetime.picker.DatePickerModal
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class TimelineViewModel(
    getToday: GetTodayUseCase,
    formatDate: FormatTimelineDateUseCase,
    getCategories: GetActiveMeasurementCategoriesUseCase,
    getValues: GetMeasurementValuesAroundDateUseCase,
    getData: GetTimelineDataUseCase,
    private val navigateToScreen: NavigateToScreenUseCase,
    private val showModal: OpenModalUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<TimelineState, TimelineIntent, TimelineEvent>() {

    private val initialDate = MutableStateFlow(getToday())
    private val currentDate = MutableStateFlow(initialDate.value)
    private val values = currentDate.flatMapLatest(getValues::invoke)
    private val categories = getCategories()
    private val data = combine(categories, values, getData::invoke)

    override val state = combine(
        initialDate,
        currentDate,
        currentDate.map(formatDate::invoke),
        data,
        ::TimelineState,
    )

    override suspend fun handleIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.CreateEntry -> navigateToScreen(EntryFormScreen())
            is TimelineIntent.SearchEntries -> navigateToScreen(EntrySearchScreen())
            is TimelineIntent.ShowDatePicker -> showDatePicker()
            is TimelineIntent.MoveDayBack -> selectDate(currentDate.value.minus(1, DateUnit.DAY))
            is TimelineIntent.MoveDayForward -> selectDate(currentDate.value.plus(1, DateUnit.DAY))
            is TimelineIntent.SetCurrentDate -> currentDate.value = intent.currentDate
        }
    }

    private fun showDatePicker() {
        showModal(
            DatePickerModal(
                date = currentDate.value,
                onPick = { date ->
                    selectDate(date)
                    closeModal()
                },
            )
        )
    }

    private fun selectDate(date: Date) {
        postEvent(TimelineEvent.DateSelected(date))
    }
}