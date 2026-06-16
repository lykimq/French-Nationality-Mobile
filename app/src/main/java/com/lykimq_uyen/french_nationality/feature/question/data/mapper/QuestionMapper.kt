package com.lykimq_uyen.french_nationality.feature.question.data.mapper

import com.lykimq_uyen.french_nationality.data.db.entity.QuestionEntity
import com.lykimq_uyen.french_nationality.feature.question.domain.model.Question

fun QuestionEntity.toQuestion(): Question {
    return Question(
        id = id,
        question = question,
        explanation = explanation,
    )
}

fun List<QuestionEntity>.toQuestions(): List<Question> {
    return map { it.toQuestion() }
}
