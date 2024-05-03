package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeConstants
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineData

@Suppress("FunctionName")
fun DrawScope.TimelineTable(
    data: TimelineData.Table,
    initialDate: Date,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    val dateTimeBase = initialDate.atStartOfDay()

    data.rows.forEachIndexed { index, row ->
        val iconSize = config.fontSize
        val heightPerCategory = iconSize + config.padding * 2

        val x = coordinates.table.topLeft.x
        val y = coordinates.table.topLeft.y + index * heightPerCategory

        if (index > 0) {
            // Divider
            drawLine(
                color = config.gridStrokeColor,
                start = Offset(x = coordinates.table.topLeft.x, y = y),
                end = Offset(x = coordinates.table.topLeft.x + coordinates.table.size.width, y = y),
                strokeWidth = config.gridStrokeWidth,
            )
        }

        val text = row.category.icon ?: ""
        val textSize = textMeasurer.measure(text)

        // Icon background
        val path = Path()
        val rect = RoundRect(
            rect = Rect(
                left = x + config.padding / 2,
                top = y + iconSize - textSize.size.height + config.padding / 2,
                right = x + textSize.size.width + config.padding * 2,
                bottom = y + iconSize + config.padding + config.padding / 2,
            ),
            cornerRadius = config.cornerRadius,
        )
        path.addRoundRect(rect)
        drawPath(
            path = path,
            color = config.backgroundColor,
        )

        // Icon
        drawText(
            text = text,
            x = x + config.padding,
            y = y + iconSize + config.padding / 1.5f,
            size = config.fontSize,
            paint = config.fontPaint,
        )

        row.values.forEach { value ->
            val dateTime = value.dateTime
            val hour = dateTime.time.hourOfDay
            val hourPerSteps = hour / config.xStep
            val widthPerDay = coordinates.canvas.size.width
            val widthPerHour = (widthPerDay / config.xAxisLabelCount).toInt()
            val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
            val offsetInMinutes = dateTimeBase.minutesUntil(dateTime)
            val offsetOfDateTime = (offsetInMinutes / config.xAxis.step) * widthPerMinute
            // FIXME
            val valueX = coordinates.chart.topLeft.x + coordinates.scroll.x + offsetOfDateTime

            val valueXRasterized = x + config.padding + widthPerHour * hourPerSteps
            val valueY = y + heightPerCategory / 2 + config.fontSize / 2
            drawText(
                text = value.value,
                x = valueX,
                y = valueY,
                size = config.fontSize,
                paint = config.fontPaint,
            )
        }
    }
}