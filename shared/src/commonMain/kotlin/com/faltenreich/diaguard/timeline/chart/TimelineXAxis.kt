package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.drawText

@Suppress("FunctionName")
fun DrawScope.TimelineXAxis(
    state: TimelineChartState,
) = with(state) {
    val size = state.chartSize

    val widthPerDay = size.width
    val widthPerHour = (widthPerDay / xAxisLabelCount).toInt()

    val xOffset = state.offset.x.toInt()
    val xOfFirstHour = xOffset % widthPerHour
    val xOfLastHour = xOfFirstHour + (xAxisLabelCount * widthPerHour)
    // Paint one additional hour per side to support cut-off labels
    val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
    val xProgression = xStart .. xEnd step widthPerHour

    xProgression.forEach { xOfLabel ->
        val xAbsolute = -(xOffset - xOfLabel)
        val xOffsetInHours = xAbsolute / widthPerHour
        val xOffsetInHoursOfDay = ((xOffsetInHours % xAxis.last) * xAxis.step) % xAxis.last
        val hour = when {
            xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + xAxis.last
            else -> xOffsetInHoursOfDay
        }
        val x = xOfLabel.toFloat()
        if (hour == 0) {
            val xOffsetInDays = xAbsolute / widthPerDay
            val date = state.initialDate.plusDays(xOffsetInDays.toInt())
            drawDate(date, x, state)
        }
        drawHour(hour, x, state)
    }
}

private fun DrawScope.drawDate(
    date: Date,
    x: Float,
    state: TimelineChartState,
    dateTimeFormatter: DateTimeFormatter = inject(),
) = with(state) {
    drawText(
        text = dateTimeFormatter.formatDate(date),
        x = x + padding,
        y = state.timelineSize.height - padding,
        size = fontSize,
        paint = fontPaint,
    )
    // Hide date indicator initially
    if (offset.x != 0f) {
        val gradientWidth = 40f
        val gradient = Brush.horizontalGradient(
            colorStops = arrayOf(
                .0f to Color.Transparent,
                1f to gridShadowColor,
            ),
            startX = x - gradientWidth,
            endX = x,
        )
        drawRect(
            brush = gradient,
            topLeft = Offset(x = x - gradientWidth, y = 0f),
            size = Size(width = gradientWidth, height = state.timelineSize.height),
        )
    }
}

private fun DrawScope.drawHour(
    hour: Int, x: Float,
    state: TimelineChartState,
) = with(state) {
    drawLine(
        color = gridStrokeColor,
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = state.timelineOrigin.y + state.timelineSize.height - state.dateTimeSize.height + padding),
        strokeWidth = gridStrokeWidth,
    )
    drawText(
        text = hour.toString(),
        x = x + padding,
        y = state.timelineSize.height - padding - fontSize - padding,
        size = fontSize,
        paint = fontPaint,
    )
}