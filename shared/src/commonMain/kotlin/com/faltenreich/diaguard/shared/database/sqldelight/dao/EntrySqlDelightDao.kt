package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.shared.database.sqldelight.EntryQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class EntrySqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: EntrySqlDelightMapper = inject(),
) : EntryDao, SqlDelightDao<EntryQueries> {

    override fun getQueries(api: SqlDelightApi): EntryQueries {
        return api.entryQueries
    }

    override fun getAll(): Flow<List<Entry>> {
        return queries.getAll(mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): Entry? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun getByQuery(query: String): Flow<List<Entry>> {
        return queries.getByQuery(query, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun create(createdAt: DateTime, dateTime: DateTime) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = createdAt.isoString,
            date_time = dateTime.isoString,
        )
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            date_time = dateTime.isoString,
            note = note,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}