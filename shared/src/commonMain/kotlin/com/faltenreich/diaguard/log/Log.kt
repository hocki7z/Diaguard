package com.faltenreich.diaguard.log

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.item.LogDay
import com.faltenreich.diaguard.log.item.LogDayStyle
import com.faltenreich.diaguard.log.item.LogEmpty
import com.faltenreich.diaguard.log.item.LogEntry
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.log.item.LogMonth
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.Skeleton
import com.faltenreich.diaguard.shared.view.collectAsPaginationItems
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlin.math.min

@Composable
fun Log(
    modifier: Modifier = Modifier,
    viewModel: LogViewModel = inject(),
) {
    // FIXME: Gets not updated on entry change
    val state = viewModel.collectState() ?: return
    val items = viewModel.pagingData.collectAsPaginationItems()
    val listState = rememberLazyListState()

    // Compensate initial scroll offset for month header
    // TODO: Find way to avoid initial jumps
    LaunchedEffect(state.monthHeaderHeight) {
        listState.scrollBy(-state.monthHeaderHeight.toFloat())
    }

    LaunchedEffect(state) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .filter { it.offset > state.monthHeaderHeight }
                .takeIf(List<*>::isNotEmpty)
        }.distinctUntilChanged().filterNotNull().collect { nextItems ->
            val firstItem = items.get(nextItems.first().index - 1)
                ?: return@collect

            viewModel.currentDate.value = firstItem.date

            if (firstItem is LogItem.MonthHeader) {
                viewModel.dispatchIntent(
                    LogIntent.SetStickyHeaderInfo(
                        state.stickyDayInfo.copy(
                            offset = IntOffset(x = 0, y = -state.dayHeaderHeight),
                        )
                    )
                )
                return@collect
            }

            val nextItem = nextItems.firstOrNull { item ->
                when (val key = item.key) {
                    is LogKey.Header -> true
                    is LogKey.Item -> key.isFirstOfDay && key.date > firstItem.date
                    else -> true
                }
            }
            val offset = when (nextItem?.key) {
                is LogKey.Header -> -state.dayHeaderHeight
                is LogKey.Item -> min(state.monthHeaderHeight, nextItem.offset - state.dayHeaderHeight)
                else -> -state.dayHeaderHeight
            }

            val date = firstItem.date
            val style = firstItem.style.takeIf { it != LogDayStyle.HIDDEN } ?: LogDayStyle.NORMAL
            val overlap = -(offset - state.monthHeaderHeight)
            val clip = if (overlap > 0) overlap.toFloat() else 0f

            viewModel.dispatchIntent(
                LogIntent.SetStickyHeaderInfo(
                    state.stickyDayInfo.copy(
                        date = date,
                        style = style,
                        offset = IntOffset(x = 0, y = offset),
                        clip = clip,
                    )
                )
            )
        }
    }

    Box {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState,
        ) {
            for (index in 0 until items.itemCount) {
                when (val peek = items.peek(index)) {
                    is LogItem.MonthHeader -> stickyHeader(key = peek.key) {
                        val item = items.get(index) as? LogItem.MonthHeader
                        checkNotNull(item)
                        LogMonth(
                            item = item,
                            modifier = Modifier.onGloballyPositioned {
                                viewModel.dispatchIntent(LogIntent.SetMonthHeaderHeight(it.size.height))
                            },
                        )
                    }

                    is LogItem.EntryContent -> item(key = peek.key) {
                        val item = items.get(index) as? LogItem.EntryContent
                        checkNotNull(item)
                        LogEntry(
                            item = item,
                            onClick = { viewModel.dispatchIntent(LogIntent.OpenEntry(item.entry)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = AppTheme.dimensions.padding.P_3,
                                    vertical = AppTheme.dimensions.padding.P_2,
                                ),
                        )
                    }

                    is LogItem.EmptyContent -> item(key = peek.key) {
                        val item = items.get(index) as? LogItem.EmptyContent
                        checkNotNull(item)
                        LogEmpty(
                            item = item,
                            onIntent = viewModel::dispatchIntent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = AppTheme.dimensions.padding.P_3),
                        )
                    }

                    null -> item {
                        Skeleton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppTheme.dimensions.size.TouchSizeMedium),
                        )
                    }
                }
            }
        }

        state.stickyDayInfo.date?.let { date ->
            LogDay(
                date = date,
                style = state.stickyDayInfo.style,
                modifier = Modifier
                    .onGloballyPositioned {
                        viewModel.dispatchIntent(LogIntent.SetDayHeaderHeight(it.size.height))
                    }
                    .offset { state.stickyDayInfo.offset }
                    .drawWithContent {
                        clipRect(top = state.stickyDayInfo.clip) {
                            this@drawWithContent.drawContent()
                        }
                    }
                    .background(AppTheme.colors.scheme.surface)
                    .padding(all = AppTheme.dimensions.padding.P_3)
            )
        }
    }
}