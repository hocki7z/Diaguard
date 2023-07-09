package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@Suppress("FunctionName")
fun DrawScope.TimelineXAxis(
    origin: Offset,
    size: Size,
    timeOrigin: Offset,
    timeSize: Size,
    dateOrigin: Offset,
    dateSize: Size,
    offset: Offset,
    config: TimelineConfig,
) = with(config) {
    drawRect(
        color = gridShadowColor,
        topLeft = timeOrigin,
        size = Size(
            width = timeSize.width,
            height = timeSize.height + dateSize.height,
        ),
        style = Fill,
    )

    val widthPerDay = size.width
    val widthPerHour = (widthPerDay / xAxisLabelCount).toInt()

    val xOffset = offset.x.toInt()
    val xOfFirstHour = xOffset % widthPerHour
    val xOfLastHour = xOfFirstHour + (xAxisLabelCount * widthPerHour)
    // Paint one additional hour per side to support cut-off labels
    val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
    val xOfHours = xStart .. xEnd step widthPerHour

    xOfHours.forEach { xOfLabel ->
        val xAbsolute = -(xOffset - xOfLabel)
        val xOffsetInHours = xAbsolute / widthPerHour
        val xOffsetInHoursOfDay = ((xOffsetInHours % xAxis.last) * xAxis.step) % xAxis.last
        val hour = when {
            xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + xAxis.last
            else -> xOffsetInHoursOfDay
        }
        val x = xOfLabel.toFloat()
        if (hour == xAxis.first) {
            // Hide date indicator initially
            if (offset.x != 0f) {
                drawDateIndicator(config, x)
            }
        }
        drawHour(origin, size, timeOrigin, timeSize, dateOrigin, dateSize, offset, config, hour, x)
    }
    drawDates(dateOrigin, dateSize, offset, config)
}

private fun DrawScope.drawDates(
    origin: Offset,
    size: Size,
    offset: Offset,
    config: TimelineConfig,
) = with(config) {
    val widthPerDay = size.width

    val xOfFirstHour = offset.x % widthPerDay
    val xOffsetInDays = -floor(offset.x / widthPerDay).toInt()

    // FIXME: Date gets shifted when indicator is at second half of screen
    val secondDate = initialDate.plusDays(xOffsetInDays)
    val firstDate = secondDate.minusDays(1)

    val firstDateAsText = "%s, %s".format(
        daysOfWeek[firstDate.dayOfWeek],
        dateTimeFormatter.formatDate(firstDate),
    )
    val secondDateAsText = "%s, %s".format(
        daysOfWeek[secondDate.dayOfWeek],
        dateTimeFormatter.formatDate(secondDate),
    )

    val firstDateTextWidth = textMeasurer.measure(firstDateAsText).size.width
    val secondDateTextWidth = textMeasurer.measure(secondDateAsText).size.width

    val xCenterOfFirstDate = xOfFirstHour - size.width / 2 - firstDateTextWidth / 2
    // TODO: Show previous date

    val xCenterOfSecondDate = xOfFirstHour + size.width / 2 - secondDateTextWidth / 2

    drawText(
        text = secondDateAsText,
        x = max(min(xCenterOfSecondDate, size.width - secondDateTextWidth - padding * 4), xOfFirstHour + padding),
        y = origin.y + size.height / 2 + fontSize / 2,
        size = fontSize,
        paint = fontPaint,
    )
}

private fun DrawScope.drawDateIndicator(
    config: TimelineConfig,
    x: Float,
) = with(config) {
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
        size = Size(width = gradientWidth, height = size.height),
    )
}

private fun DrawScope.drawDate(
    dateOrigin: Offset,
    dateSize: Size,
    config: TimelineConfig,
    date: Date,
    x: Float,
) = with(config) {
    val text = "%s, %s".format(
        daysOfWeek[date.dayOfWeek],
        dateTimeFormatter.formatDate(date),
    )
    val textWidth = textMeasurer.measure(text).size.width
    val xCenterOfDate = x - textWidth / 2
    val xMax = dateSize.width - textWidth - padding * 4 // FIXME: Why multiplier of 4?
    drawText(
        text = text,
        x = min(xCenterOfDate, xMax),
        y = dateOrigin.y + dateSize.height / 2 + fontSize / 2,
        size = fontSize,
        paint = fontPaint,
    )
}

private fun DrawScope.drawHour(
    origin: Offset,
    size: Size,
    timeOrigin: Offset,
    timeSize: Size,
    dateOrigin: Offset,
    dateSize: Size,
    offset: Offset,
    config: TimelineConfig,
    hour: Int,
    x: Float,
) = with(config) {
    val lineEndY = when (hour) {
        0 -> origin.y + size.height
        else -> origin.y + size.height - timeSize.height - dateSize.height + padding
    }
    drawLine(
        color = gridStrokeColor,
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = lineEndY),
        strokeWidth = gridStrokeWidth,
    )
    drawText(
        text = hour.toString(),
        x = x + padding,
        y = timeOrigin.y + padding + fontSize,
        size = fontSize,
        paint = fontPaint,
    )
}