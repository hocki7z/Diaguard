package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.primitive.format
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.grams_abbreviation
import kotlinx.coroutines.flow.firstOrNull

class MapEntryListItemStateUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val entryTagRepository: EntryTagRepository,
    private val foodEatenRepository: FoodEatenRepository,
    private val getPreference: GetPreferenceUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
    private val numberFormatter: NumberFormatter,
    private val measurementValueMapper: MeasurementValueMapper,
    private val localization: Localization,
) {

    suspend operator fun invoke(entry: Entry.Local): EntryListItemState {
        val decimalPlaces = getPreference(DecimalPlaces).firstOrNull() ?: DecimalPlaces.default
        return EntryListItemState(
            entry = entry.apply {
                values = valueRepository.getByEntryId(entry.id)
                entryTags = entryTagRepository.getByEntryId(entry.id)
                foodEaten = foodEatenRepository.getByEntryId(entry.id)
            },
            dateTimeLocalized = "%s, %s".format(
                dateTimeFormatter.formatMonth(entry.dateTime.date.month, abbreviated = true),
                dateTimeFormatter.formatDateTime(entry.dateTime),
            ),
            foodEatenLocalized = entry.foodEaten.map { foodEaten ->
                "%s %s %s".format(
                    numberFormatter(
                        number = foodEaten.amountInGrams,
                        scale = decimalPlaces,
                        locale = localization.getLocale(),
                    ),
                    localization.getString(Res.string.grams_abbreviation),
                    foodEaten.food.name,
                )
            },
            values = entry.values.map { value ->
                EntryListItemState.Value(
                    category = value.property.category,
                    valueLocalized = measurementValueMapper(value, decimalPlaces).value,
                )
            },
        )
    }
}