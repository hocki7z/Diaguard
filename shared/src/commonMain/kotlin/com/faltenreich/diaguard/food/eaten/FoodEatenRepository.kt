package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class FoodEatenRepository(
    private val dao: FoodEatenDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(foodEaten: FoodEaten.Legacy): Long = with(foodEaten) {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            amountInGrams = amountInGrams,
            foodId = food.id,
            entryId = entry.id,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(foodEaten: FoodEaten.Intermediate): Long = with(foodEaten) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            amountInGrams = amountInGrams,
            foodId = food.id,
            entryId = entry.id,
        )
        return checkNotNull(dao.getLastId())
    }

    fun observeByFoodId(foodId: Long): Flow<List<FoodEaten.Local>> {
        return dao.observeByFoodId(foodId)
    }

    fun getByEntryId(entryId: Long): List<FoodEaten.Local> {
        return dao.getByEntryId(entryId)
    }

    fun update(foodEaten: FoodEaten.Local) = with(foodEaten) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            amountInGrams = amountInGrams,
        )
    }

    fun delete(foodEaten: FoodEaten.Local) {
        dao.deleteById(foodEaten.id)
    }
}