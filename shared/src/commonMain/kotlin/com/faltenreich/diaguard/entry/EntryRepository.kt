package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class EntryRepository(
    private val dao: EntryDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        dateTime: DateTime,
        note: String?,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            dateTime = dateTime,
            note = note,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(dateTime: DateTime): Long {
        val now = dateTimeFactory.now()
        return create(
            createdAt = now,
            updatedAt = now,
            dateTime = dateTime,
            note = null,
        )
    }

    fun getById(id: Long): Entry.Local? {
        return dao.getById(id)
    }

    fun getByDateRange(startDateTime: DateTime, endDateTime: DateTime): List<Entry.Local> {
        return dao.getByDateRange(startDateTime, endDateTime)
    }

    fun getByQuery(query: String): Flow<List<Entry.Local>> {
        return dao.getByQuery(query)
    }

    fun getAll(): Flow<List<Entry.Local>> {
        return dao.getAll()
    }

    fun countAll(): Flow<Long> {
        return dao.countAll()
    }

    fun update(
        id: Long,
        dateTime: DateTime,
        note: String?,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            dateTime = dateTime,
            note = note,
        )
    }

    fun update(entry: Entry.Local) {
        update(
            id = entry.id,
            dateTime = entry.dateTime,
            note = entry.note,
        )
    }

    fun delete(entry: Entry.Local) {
        dao.deleteById(entry.id)
    }
}