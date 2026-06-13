package com.lykimq_uyen.french_nationality.feature.home.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lykimq_uyen.french_nationality.core.di.AppContainer
import com.lykimq_uyen.french_nationality.feature.home.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repository: CategoryRepository =
        AppContainer.categoryRepository(application)

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            _uiState.value = runCatching { repository.getCategories() }
                .fold(
                    onSuccess = { HomeUiState.Success(it) },
                    onFailure = { HomeUiState.Error(it.message ?: "Erreur inconnue") },
                )
        }
    }
}
