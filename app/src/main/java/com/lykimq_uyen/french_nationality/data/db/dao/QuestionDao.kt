package com.lykimq_uyen.french_nationality.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.lykimq_uyen.french_nationality.data.db.entity.QuestionEntity

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE sub_category_id = :subCategoryId ORDER BY sort_order ASC")
    suspend fun getBySubCategoryId(subCategoryId: String): List<QuestionEntity>
}
