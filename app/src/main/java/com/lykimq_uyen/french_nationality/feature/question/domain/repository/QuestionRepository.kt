package com.lykimq_uyen.french_nationality.feature.question.domain.repository

import com.lykimq_uyen.french_nationality.feature.question.domain.model.Question

interface QuestionRepository {
    suspend fun getQuestions(subCategoryId: String): List<Question>
}
