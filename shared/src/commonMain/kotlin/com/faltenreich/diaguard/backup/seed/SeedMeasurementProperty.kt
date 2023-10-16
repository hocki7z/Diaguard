package com.faltenreich.diaguard.backup.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedMeasurementProperty(
    val name: SeedLocalization,
    val key: String,
    val icon: String,
    val types: List<SeedMeasurementType>,
)