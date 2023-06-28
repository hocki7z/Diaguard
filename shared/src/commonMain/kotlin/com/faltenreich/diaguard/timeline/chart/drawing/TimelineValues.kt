package com.faltenreich.diaguard.timeline.chart.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeConstants
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.view.bezierBetween
import com.faltenreich.diaguard.timeline.chart.TimelineChartConfig

@Suppress("FunctionName")
fun DrawScope.TimelineValues(
    offset: Offset,
    initialDate: Date,
    values: List<MeasurementValue>,
    config: TimelineChartConfig,
) = with(config) {
    val coordinates = values.map { value ->
        val dateTimeBase = initialDate.atTime(Time.atStartOfDay())
        val dateTime = value.entry.dateTime
        val widthPerDay = size.width
        val widthPerHour = widthPerDay / (xAxis.last / xAxis.step)
        val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
        val offsetInMinutes = dateTimeBase.minutesUntil(dateTime)
        val offsetOfDateTime = (offsetInMinutes / xAxis.step) * widthPerMinute
        val x = offset.x + offsetOfDateTime

        val percentage = (value.value - yAxis.first) / (yAxis.last - yAxis.first)
        val y = size.height - (percentage.toFloat() * size.height)

        Offset(x, y)
    }
    if (coordinates.isEmpty()) {
        return@with
    }

    // TODO: Get percentages from extremas
    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            .3f to lineColorHigh,
            .35f to lineColorNormal,
            .8f to lineColorNormal,
            .85f to lineColorLow,
        ),
    )

    val path = Path()
    path.reset()

    drawValue(coordinates.first(), brush, config)

    val style = Stroke(width = strokeWidth)
    coordinates.zipWithNext { start, end ->
        path.moveTo(start.x, start.y)
        path.bezierBetween(start, end)

        drawPath(
            path = path,
            brush = brush,
            style = style,
        )
        drawValue(end, brush, config)
    }
}

private fun DrawScope.drawValue(offset: Offset, brush: Brush, config: TimelineChartConfig) {
    drawCircle(
        brush = brush,
        radius = config.dotRadius,
        center = offset,
        style = Fill,
    )
}