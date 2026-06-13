package com.lykimq_uyen.french_nationality.feature.question.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lykimq_uyen.french_nationality.core.di.AppContainer
import com.lykimq_uyen.french_nationality.core.progress.StudyProgressRepository
import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category
import com.lykimq_uyen.french_nationality.feature.home.domain.repository.CategoryRepository
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionListItem
import com.lykimq_uyen.french_nationality.feature.question.domain.model.findByQuestionId
import com.lykimq_uyen.french_nationality.feature.question.domain.model.toNumberedItems
import com.lykimq_uyen.french_nationality.feature.question.domain.repository.QuestionRepository
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.repository.SubCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionStudyViewModel(
    application: Application,
    private val categoryId: String,
    private val subCategoryId: String,
    private val initialQuestionId: String,
) : AndroidViewModel(application) {

    private val categoryRepository: CategoryRepository =
        AppContainer.categoryRepository(application)
    private val subCategoryRepository: SubCategoryRepository =
        AppContainer.subCategoryRepository(application)
    private val questionRepository: QuestionRepository =
        AppContainer.questionRepository(application)
    private val studyProgressRepository: StudyProgressRepository =
        AppContainer.studyProgressRepository(application)

    private val _uiState = MutableStateFlow<QuestionStudyUiState>(QuestionStudyUiState.Loading)
    val uiState: StateFlow<QuestionStudyUiState> = _uiState.asStateFlow()

    init {
        loadStudySession()
    }

    fun goToNext() {
        updateCurrentItem { current, items ->
            items.getOrNull(current.number)?.let { nextItem ->
                applyCurrentItem(items, nextItem)
            }
        }
    }

    fun goToPrevious() {
        updateCurrentItem { current, items ->
            items.getOrNull(current.number - 2)?.let { previousItem ->
                applyCurrentItem(items, previousItem)
            }
        }
    }

    fun goToQuestionNumber(number: Int) {
        updateCurrentItem { _, items ->
            items.getOrNull(number - 1)?.let { item ->
                applyCurrentItem(items, item)
            }
        }
    }

    private fun loadStudySession() {
        viewModelScope.launch {
            _uiState.value = QuestionStudyUiState.Loading
            _uiState.value = runCatching {
                val category = categoryRepository.getCategoryById(categoryId)
                    ?: error("Categorie introuvable")
                val subCategory = subCategoryRepository.getSubCategoryById(subCategoryId)
                    ?: error("Section introuvable")
                val items = questionRepository.getQuestions(subCategoryId).toNumberedItems()
                if (items.isEmpty()) {
                    error("Aucune question dans cette section")
                }
                val currentItem = items.findByQuestionId(initialQuestionId)
                    ?: items.first()
                applyCurrentItem(items, currentItem, category, subCategory)
            }.fold(
                onSuccess = { it },
                onFailure = { QuestionStudyUiState.Error(it.message ?: "Erreur inconnue") },
            )
        }
    }

    private fun updateCurrentItem(
        block: (QuestionListItem, List<QuestionListItem>) -> QuestionStudyUiState.Success?,
    ) {
        val currentState = _uiState.value as? QuestionStudyUiState.Success ?: return
        val nextState = block(currentState.currentItem, currentState.items) ?: return
        _uiState.value = nextState
    }

    private fun applyCurrentItem(
        items: List<QuestionListItem>,
        currentItem: QuestionListItem,
        category: Category? = null,
        subCategory: SubCategory? = null,
    ): QuestionStudyUiState.Success {
        studyProgressRepository.saveLastQuestionId(subCategoryId, currentItem.question.id)
        val previousState = _uiState.value as? QuestionStudyUiState.Success
        return QuestionStudyUiState.Success(
            category = category ?: previousState?.category ?: error("Categorie introuvable"),
            subCategory = subCategory ?: previousState?.subCategory ?: error("Section introuvable"),
            items = items,
            currentItem = currentItem,
            canGoPrevious = currentItem.number > 1,
            canGoNext = currentItem.number < items.size,
        )
    }
}

class QuestionStudyViewModelFactory(
    private val application: Application,
    private val categoryId: String,
    private val subCategoryId: String,
    private val questionId: String,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionStudyViewModel::class.java)) {
            return QuestionStudyViewModel(application, categoryId, subCategoryId, questionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
