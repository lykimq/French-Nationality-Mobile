package com.lykimq_uyen.french_nationality.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.lykimq_uyen.french_nationality.data.db.entity.SubCategoryEntity

@Dao
interface SubCategoryDao {
    @Query("SELECT * FROM sub_categories WHERE category_id = :categoryId ORDER BY sort_order ASC")
    suspend fun getByCategoryId(categoryId: String): List<SubCategoryEntity>

    @Query("SELECT * FROM sub_categories WHERE id = :subCategoryId LIMIT 1")
    suspend fun getById(subCategoryId: String): SubCategoryEntity?

    @Query("SELECT COUNT(*) FROM sub_categories WHERE category_id = :categoryId")
    suspend fun getSubCategoryCount(categoryId: String): Int

    @Query(
        """
        SELECT COUNT(*) FROM questions
        WHERE sub_category_id = :subCategoryId
        """,
    )
    suspend fun getQuestionCount(subCategoryId: String): Int
}
