package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.serialization.Serialization

class SeedImport(
    private val reader: SeedReader,
    private val serialization: Serialization,
    private val mapper: SeedMapper,
    private val propertyRepository: MeasurementPropertyRepository,
    private val typeRepository: MeasurementTypeRepository,
    private val unitRepository: MeasurementUnitRepository,
) : Import {

    override operator fun invoke() {
        val yaml = reader()
        val seedData = serialization.decodeYaml<List<SeedMeasurementProperty>>(yaml)
        val current: SeedLocalization.() -> String = { mapper(this) }
        seedData.forEachIndexed { propertySortIndex, property ->
            val propertyId = propertyRepository.create(
                name = property.name.current(),
                icon = property.icon,
                sortIndex = propertySortIndex.toLong(),
                isUserGenerated = false,
            )
            property.types.forEachIndexed { typeSortIndex, type ->
                val typeId = typeRepository.create(
                    name = type.name.current(),
                    sortIndex = typeSortIndex.toLong(),
                    propertyId = propertyId,
                )
                type.units.forEach { unit ->
                    val unitId = unitRepository.create(
                        name = unit.name.current(),
                        factor = unit.factor,
                        typeId = typeId,
                    )
                    val isSelectedUnit = unit.factor == 1.0
                    if (isSelectedUnit) {
                        typeRepository.update(
                            id = typeId,
                            name = type.name.current(),
                            sortIndex = typeSortIndex.toLong(),
                            selectedUnitId = unitId,
                        )
                    }
                }
            }
        }
    }
}