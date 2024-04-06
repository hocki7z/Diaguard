package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed interface MeasurementPropertyListViewState {

    class Loaded(val items: List<MeasurementProperty>) : MeasurementPropertyListViewState
}