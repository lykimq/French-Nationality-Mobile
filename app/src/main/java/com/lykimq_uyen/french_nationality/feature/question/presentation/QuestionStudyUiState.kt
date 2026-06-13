package com.lykimq_uyen.french_nationality.feature.question.presentation

import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionListItem
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory

sealed interface QuestionStudyUiState {
    data object Loading : QuestionStudyUiState

    data class Success(
        val category: Category,
        val subCategory: SubCategory,
        val items: List<QuestionListItem>,
        val currentItem: QuestionListItem,
        val canGoPrevious: Boolean,
        val canGoNext: Boolean,
    ) : QuestionStudyUiState

    data class Error(
        val message: String,
    ) : QuestionStudyUiState
}
