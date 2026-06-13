package com.lykimq_uyen.french_nationality.feature.home.presentation

import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val categories: List<Category>,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}
