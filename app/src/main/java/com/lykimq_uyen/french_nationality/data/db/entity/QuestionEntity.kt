package com.lykimq_uyen.french_nationality.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    indices = [
        Index(value = ["category_id", "source"], name = "idx_questions_category"),
        Index(value = ["sub_category_id"], name = "idx_questions_sub_category"),
        Index(value = ["content_fingerprint"], name = "idx_questions_fingerprint", unique = true),
    ],
)
data class QuestionEntity(
    @PrimaryKey val id: String,
    val category_id: String,
    val sub_category_id: String,
    val question: String,
    val explanation: String,
    val image_path: String?,
    val source: String,
    val question_type: String?,
    val sort_order: Int,
    val content_fingerprint: String,
)
