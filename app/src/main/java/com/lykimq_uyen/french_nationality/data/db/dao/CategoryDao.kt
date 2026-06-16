package com.lykimq_uyen.french_nationality.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.lykimq_uyen.french_nationality.data.db.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY sort_order ASC")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :categoryId LIMIT 1")
    suspend fun getCategoryById(categoryId: String): CategoryEntity?
}
