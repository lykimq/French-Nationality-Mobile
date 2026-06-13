package com.lykimq_uyen.french_nationality.data.db.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface QuestionDao {
    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int
}
