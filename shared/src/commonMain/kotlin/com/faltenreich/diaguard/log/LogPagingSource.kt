package com.faltenreich.diaguard.log

import app.cash.paging.PagingConfig
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.list.DateListItemStyle
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.isAppending
import com.faltenreich.diaguard.shared.view.isPrepending
import com.faltenreich.diaguard.shared.view.isRefreshing

class LogPagingSource(
    getTodayUseCase: GetTodayUseCase = inject(),
    private val entryRepository: EntryRepository = inject(),
    private val valueRepository: MeasurementValueRepository = inject(),
    private val entryTagRepository: EntryTagRepository = inject(),
    private val foodEatenRepository: FoodEatenRepository = inject(),
) : PagingSource<Date, LogItem>() {

    private val today = getTodayUseCase()

    override fun getRefreshKey(state: PagingState<Date, LogItem>): Date? {
        return state.closestItemToPosition(0)?.date
    }

    override suspend fun load(params: PagingSourceLoadParams<Date>): PagingSourceLoadResult<Date, LogItem> {
        val key = params.key ?: today
        val startDate: Date
        val endDate: Date
        when {
            params.isRefreshing() -> {
                startDate = key
                endDate = key.plus(params.loadSize, DateUnit.DAY)
            }
            params.isPrepending() -> {
                startDate = key.minus(params.loadSize, DateUnit.DAY)
                endDate = key
            }
            params.isAppending() -> {
                startDate = key
                endDate = key.plus(params.loadSize, DateUnit.DAY)
            }
            else -> throw IllegalArgumentException("Unhandled parameters: $params")
        }

        val entries = entryRepository.getByDateRange(
            startDateTime = startDate.atStartOfDay(),
            endDateTime = endDate.atEndOfDay(),
        ).map { entry ->
            entry.apply {
                values = valueRepository.getByEntryId(entry.id)
                entryTags = entryTagRepository.getByEntryId(entry.id)
                foodEaten = foodEatenRepository.getByEntryId(entry.id)
            }
        }

        val items = DateProgression(startDate, endDate).map { date ->
            val headers = listOfNotNull(LogItem.MonthHeader(date).takeIf { date.dayOfMonth == 1 })
            val entriesOfDate = entries.filter { it.dateTime.date == date }
            val entryContent = entriesOfDate.takeIf(List<Entry>::isNotEmpty)?.map { entry ->
                LogItem.EntryContent(
                    entry = entry,
                    style = DateListItemStyle(
                        isVisible = entry == entriesOfDate.first(),
                        isHighlighted = entry.dateTime.date == today,
                    ),
                )
            }
            val content = entryContent ?: listOf(
                LogItem.EmptyContent(
                    date = date,
                    style = DateListItemStyle(
                        isVisible = true,
                        isHighlighted = date == today,
                    ),
                ),
            )
            headers + content
        }.flatten()

        val page = PagingSourceLoadResultPage(
            data = items,
            prevKey = startDate.minus(1, DateUnit.DAY),
            nextKey = endDate.plus(1, DateUnit.DAY),
        )
        return page
    }

    companion object {

        // Must be at least the maximum day count per month to ensure a prepending header
        private const val PAGE_SIZE_IN_DAYS = 31

        fun newConfig(): PagingConfig {
            return PagingConfig(pageSize = PAGE_SIZE_IN_DAYS)
        }
    }
}