package com.lykimq_uyen.french_nationality.core.progress

interface StudyProgressRepository {
    fun getLastQuestionId(subCategoryId: String): String?

    fun saveLastQuestionId(subCategoryId: String, questionId: String)
}
