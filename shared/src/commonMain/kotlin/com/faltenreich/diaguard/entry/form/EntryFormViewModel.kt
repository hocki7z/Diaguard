package com.faltenreich.diaguard.entry.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.measurement.GetMeasurementsUseCase
import com.faltenreich.diaguard.entry.form.measurement.MeasurementInputViewState
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class EntryFormViewModel(
    entry: Entry?,
    date: Date?,
    private val dispatcher: CoroutineDispatcher = inject(),
    private val submitEntry: SubmitEntryUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
    getMeasurementsUseCase: GetMeasurementsUseCase = inject(),
) : ViewModel() {

    private val id: Long? = entry?.id

    var dateTime: DateTime by mutableStateOf(entry?.dateTime ?: date?.atTime(DateTime.now().time) ?: DateTime.now())
        private set
    var date: Date
        get() = dateTime.date
        set(value) { dateTime = dateTime.time.atDate(value) }
    var time: Time
        get() = dateTime.time
        set(value) { dateTime = dateTime.date.atTime(value) }

    var note: String by mutableStateOf(entry?.note ?: "")

    var measurements: MeasurementInputViewState by mutableStateOf(getMeasurementsUseCase(entry))

    val isEditing: Boolean
        get() = id != null

    fun updateMeasurementValue(update: MeasurementInputViewState.Property.Value) {
        measurements = measurements.copy(properties = measurements.properties.map { property ->
            property.copy(values = property.values.map { value ->
                when (value.type) {
                    update.type -> update
                    else -> value
                }
            })
        })
    }

    fun submit() = viewModelScope.launch(dispatcher) {
        submitEntry(
            id = id,
            dateTime = dateTime,
            note = note,
            measurements = measurements,
        )
    }

    fun delete() = viewModelScope.launch(dispatcher) {
        val id = id ?: throw IllegalStateException("Cannot delete entry without id")
        deleteEntry(id)
    }
}