package com.lykimq_uyen.french_nationality.feature.question.presentation

import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionChunk
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionListItem
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory

sealed interface QuestionListUiState {
    data object Loading : QuestionListUiState

    data class Success(
        val category: Category,
        val subCategory: SubCategory,
        val items: List<QuestionListItem>,
        val chunks: List<QuestionChunk>,
        val lastQuestionId: String?,
        val isLargeList: Boolean,
    ) : QuestionListUiState

    data class Error(
        val message: String,
    ) : QuestionListUiState
}
