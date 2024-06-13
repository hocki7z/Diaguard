package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag

/**
 * Implementation is stubbed since there is no legacy on iOS to import from
 */
actual class LegacySqliteDao : LegacyDao {

    override fun getEntries(): List<Entry.Legacy> {
        return emptyList()
    }

    override fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        return emptyList()
    }

    override fun getFood(): List<Food.Legacy> {
        return emptyList()
    }

    override fun getFoodEaten(): List<FoodEaten.Legacy> {
        return emptyList()
    }

    override fun getTags(): List<Tag.Legacy> {
        return emptyList()
    }
}