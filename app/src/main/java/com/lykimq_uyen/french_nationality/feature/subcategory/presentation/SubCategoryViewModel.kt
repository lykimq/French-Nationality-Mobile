package com.lykimq_uyen.french_nationality.feature.subcategory.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lykimq_uyen.french_nationality.core.di.AppContainer
import com.lykimq_uyen.french_nationality.feature.home.domain.repository.CategoryRepository
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.repository.SubCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubCategoryViewModel(
    application: Application,
    private val categoryId: String,
) : AndroidViewModel(application) {

    private val categoryRepository: CategoryRepository =
        AppContainer.categoryRepository(application)
    private val subCategoryRepository: SubCategoryRepository =
        AppContainer.subCategoryRepository(application)

    private val _uiState = MutableStateFlow<SubCategoryUiState>(SubCategoryUiState.Loading)
    val uiState: StateFlow<SubCategoryUiState> = _uiState.asStateFlow()

    init {
        loadSubCategories()
    }

    fun loadSubCategories() {
        viewModelScope.launch {
            _uiState.value = SubCategoryUiState.Loading
            _uiState.value = runCatching {
                val category = categoryRepository.getCategoryById(categoryId)
                    ?: error("Categorie introuvable")
                val subCategories = subCategoryRepository.getSubCategories(categoryId)
                SubCategoryUiState.Success(
                    category = category,
                    subCategories = subCategories,
                )
            }.fold(
                onSuccess = { it },
                onFailure = { SubCategoryUiState.Error(it.message ?: "Erreur inconnue") },
            )
        }
    }
}

class SubCategoryViewModelFactory(
    private val application: Application,
    private val categoryId: String,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubCategoryViewModel::class.java)) {
            return SubCategoryViewModel(application, categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
