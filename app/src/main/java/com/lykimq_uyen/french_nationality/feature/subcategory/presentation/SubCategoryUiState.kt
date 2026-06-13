package com.lykimq_uyen.french_nationality.feature.subcategory.presentation

import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory

sealed interface SubCategoryUiState {
    data object Loading : SubCategoryUiState

    data class Success(
        val category: Category,
        val subCategories: List<SubCategory>,
    ) : SubCategoryUiState

    data class Error(
        val message: String,
    ) : SubCategoryUiState
}
