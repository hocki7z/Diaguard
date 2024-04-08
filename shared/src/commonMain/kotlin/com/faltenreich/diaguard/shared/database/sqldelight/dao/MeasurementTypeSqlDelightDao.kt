package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeDao
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementTypeQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteLong
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementTypeSqlDelightMapper
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementTypeSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementTypeSqlDelightMapper = inject(),
) : MeasurementTypeDao, SqlDelightDao<MeasurementTypeQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementTypeQueries {
        return api.measurementTypeQueries
    }

    override fun create(
        createdAt: DateTime,
        key: String?,
        name: String,
        sortIndex: Long,
        range: MeasurementValueRange,
        selectedUnitId: Long,
        categoryId: Long,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = createdAt.isoString,
            key = key,
            name = name,
            valueRangeMinimum = range.minimum,
            valueRangeLow = range.low,
            valueRangeTarget = range.target,
            valueRangeHigh = range.high,
            valueRangeMaximum = range.maximum,
            isValueRangeHighlighted = range.isHighlighted.toSqlLiteLong(),
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
            categoryId = categoryId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): MeasurementType? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<MeasurementType?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getByKey(key: String): MeasurementType? {
        return queries.getByKey(key, mapper::map).executeAsOneOrNull()
    }

    override fun getByCategoryId(categoryId: Long): List<MeasurementType> {
        return queries.getByCategory(categoryId, mapper::map).executeAsList()
    }

    override fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementType>> {
        return queries.getByCategory(categoryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getAll(): List<MeasurementType> {
        return queries.getAll(mapper::map).executeAsList()
    }

    override fun observeAll(): Flow<List<MeasurementType>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        range: MeasurementValueRange,
        selectedUnitId: Long,
    ) {
        queries.update(
            updatedAt = updatedAt.isoString,
            name = name,
            valueRangeMinimum = range.minimum,
            valueRangeLow = range.low,
            valueRangeTarget = range.target,
            valueRangeHigh = range.high,
            valueRangeMaximum = range.maximum,
            isValueRangeHighlighted = range.isHighlighted.toSqlLiteLong(),
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}