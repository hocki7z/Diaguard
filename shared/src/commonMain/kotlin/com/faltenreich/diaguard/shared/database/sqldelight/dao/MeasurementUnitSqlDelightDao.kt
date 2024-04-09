package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementUnitQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementUnitSqlDelightMapper
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementUnitSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementUnitSqlDelightMapper = inject(),
) : MeasurementUnitDao, SqlDelightDao<MeasurementUnitQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementUnitQueries {
        return api.measurementUnitQueries
    }

    override fun create(
        createdAt: DateTime,
        key: String?,
        name: String,
        abbreviation: String,
        factor: Double,
        propertyId: Long,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = createdAt.isoString,
            key = key,
            name = name,
            abbreviation = abbreviation,
            factor = factor,
            propertyId = propertyId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<MeasurementUnit?> {
        return queries.getById(id, mapper::map).asFlow().mapToOneOrNull(dispatcher)
    }

    override fun getByKey(key: String): MeasurementUnit? {
        return queries.getByKey(key, mapper::map).executeAsOneOrNull()
    }

    override fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementUnit>> {
        return queries.getByProperty(propertyId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementUnit>> {
        return queries.getByCategoryId(categoryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeAll(): Flow<List<MeasurementUnit>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        abbreviation: String,
    ) {
        queries.update(
            updatedAt = updatedAt.isoString,
            name = name,
            abbreviation = abbreviation,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}