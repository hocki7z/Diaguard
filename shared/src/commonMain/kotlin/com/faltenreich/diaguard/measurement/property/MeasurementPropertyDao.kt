package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementPropertyDao {

    fun create(
        createdAt: DateTime,
        name: String,
        key: String?,
        icon: String?,
        sortIndex: Long,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementProperty?

    fun observeById(id: Long): Flow<MeasurementProperty?>

    fun getAll(): List<MeasurementProperty>

    fun observeAll(): Flow<List<MeasurementProperty>>

    fun countAll(): Flow<Long>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
    )

    fun deleteById(id: Long)
}