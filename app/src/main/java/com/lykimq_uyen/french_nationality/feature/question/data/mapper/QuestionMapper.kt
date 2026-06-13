package com.lykimq_uyen.french_nationality.feature.question.data.mapper

import com.lykimq_uyen.french_nationality.data.db.entity.QuestionEntity
import com.lykimq_uyen.french_nationality.feature.question.domain.model.Question

fun QuestionEntity.toQuestion(): Question {
    return Question(
        id = id,
        subCategoryId = sub_category_id,
        categoryId = category_id,
        question = question,
        explanation = explanation,
        sortOrder = sort_order,
    )
}

fun List<QuestionEntity>.toQuestions(): List<Question> {
    return map { it.toQuestion() }
}
