package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.Measurement
import com.faltenreich.diaguard.measurement.MeasurementDao
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.sqldelight.MeasurementQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class MeasurementSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: MeasurementSqlDelightMapper = inject(),
) : MeasurementDao, SqlDelightDao<MeasurementQueries> {

    override fun getQueries(api: SqlDelightApi): MeasurementQueries {
        return api.measurementQueries
    }

    override fun create(
        createdAt: DateTime,
        type: MeasurementType,
        entry: Entry,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            type_id = type.id,
            entry_id = entry.id,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getByEntry(entry: Entry): Flow<List<Measurement>> {
        return queries.getByEntry(entry.id, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}