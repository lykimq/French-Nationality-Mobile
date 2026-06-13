package com.lykimq_uyen.french_nationality.feature.question.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lykimq_uyen.french_nationality.core.di.AppContainer
import com.lykimq_uyen.french_nationality.feature.home.domain.repository.CategoryRepository
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionListConfig
import com.lykimq_uyen.french_nationality.feature.question.domain.model.toChunks
import com.lykimq_uyen.french_nationality.feature.question.domain.model.toNumberedItems
import com.lykimq_uyen.french_nationality.feature.question.domain.repository.QuestionRepository
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.repository.SubCategoryRepository
import com.lykimq_uyen.french_nationality.core.progress.StudyProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionListViewModel(
    application: Application,
    private val categoryId: String,
    private val subCategoryId: String,
) : AndroidViewModel(application) {

    private val categoryRepository: CategoryRepository =
        AppContainer.categoryRepository(application)
    private val subCategoryRepository: SubCategoryRepository =
        AppContainer.subCategoryRepository(application)
    private val questionRepository: QuestionRepository =
        AppContainer.questionRepository(application)
    private val studyProgressRepository: StudyProgressRepository =
        AppContainer.studyProgressRepository(application)

    private val _uiState = MutableStateFlow<QuestionListUiState>(QuestionListUiState.Loading)
    val uiState: StateFlow<QuestionListUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            _uiState.value = QuestionListUiState.Loading
            _uiState.value = runCatching {
                val category = categoryRepository.getCategoryById(categoryId)
                    ?: error("Categorie introuvable")
                val subCategory = subCategoryRepository.getSubCategoryById(subCategoryId)
                    ?: error("Section introuvable")
                val items = questionRepository.getQuestions(subCategoryId).toNumberedItems()
                val isLargeList = items.size > QuestionListConfig.LARGE_LIST_THRESHOLD
                val chunks = if (isLargeList) {
                    items.toChunks(QuestionListConfig.CHUNK_SIZE)
                } else {
                    emptyList()
                }
                QuestionListUiState.Success(
                    category = category,
                    subCategory = subCategory,
                    items = items,
                    chunks = chunks,
                    lastQuestionId = studyProgressRepository.getLastQuestionId(subCategoryId),
                    isLargeList = isLargeList,
                )
            }.fold(
                onSuccess = { it },
                onFailure = { QuestionListUiState.Error(it.message ?: "Erreur inconnue") },
            )
        }
    }
}

class QuestionListViewModelFactory(
    private val application: Application,
    private val categoryId: String,
    private val subCategoryId: String,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionListViewModel::class.java)) {
            return QuestionListViewModel(application, categoryId, subCategoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
