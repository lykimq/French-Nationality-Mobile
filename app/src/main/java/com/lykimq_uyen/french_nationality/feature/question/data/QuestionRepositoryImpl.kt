package com.lykimq_uyen.french_nationality.feature.question.data

import com.lykimq_uyen.french_nationality.data.db.dao.QuestionDao
import com.lykimq_uyen.french_nationality.feature.question.data.mapper.toQuestions
import com.lykimq_uyen.french_nationality.feature.question.domain.model.Question
import com.lykimq_uyen.french_nationality.feature.question.domain.repository.QuestionRepository

class QuestionRepositoryImpl(
    private val questionDao: QuestionDao,
) : QuestionRepository {

    override suspend fun getQuestions(subCategoryId: String): List<Question> {
        return questionDao.getBySubCategoryId(subCategoryId).toQuestions()
    }
}
