package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.shared.database.sqldelight.EntryQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class EntrySqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
) : EntryDao, SqlDelightDao<EntryQueries> {

    override fun getQueries(api: SqlDelightApi): EntryQueries {
        return api.entryQueries
    }

    override fun getAll(): Flow<List<Entry>> {
        return queries.getAll { id, createdAt, updatedAt, note ->
            Entry(
                id = id,
                createdAt = DateTime(isoString = createdAt),
                updatedAt = DateTime(isoString = updatedAt),
                note = note,
            )
        }.asFlow().mapToList(dispatcher)
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getById(id: Long): Entry? {
        return queries.getById(id) { _, createdAt, updatedAt, note ->
            Entry(
                id = id,
                createdAt = DateTime(isoString = createdAt),
                updatedAt = DateTime(isoString = updatedAt),
                note = note,
            )
        }.executeAsOneOrNull()
    }

    override fun getByQuery(query: String): Flow<List<Entry>> {
        return queries.getByQuery(query) { id, createdAt, updatedAt, note ->
            Entry(
                id = id,
                createdAt = DateTime(isoString = createdAt),
                updatedAt = DateTime(isoString = updatedAt),
                note = note,
            )
        }.asFlow().mapToList(dispatcher)
    }

    override fun create(dateTime: DateTime) {
        val dateTimeIsoString = dateTime.isoString
        queries.create(
            createdAt = dateTimeIsoString,
            updatedAt = dateTimeIsoString,
        )
    }

    override fun update(entry: Entry) {
        queries.update(
            id = entry.id,
            createdAt = entry.createdAt.isoString,
            updatedAt = entry.updatedAt.isoString,
            note = entry.note,
        )
    }

    override fun delete(entry: Entry) {
        queries.deleteById(id = entry.id)
    }
}