package com.faltenreich.diaguard.backup.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedMeasurementProperty(
    val key: String,
    val name: SeedLocalization,
    val icon: String,
    val types: List<SeedMeasurementType>,
)