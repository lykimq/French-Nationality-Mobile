package com.lykimq_uyen.french_nationality.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "answers",
    indices = [Index(value = ["question_id"], name = "idx_answers_question")],
)
data class AnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val question_id: String,
    val text: String,
    val is_correct: Int,
)
