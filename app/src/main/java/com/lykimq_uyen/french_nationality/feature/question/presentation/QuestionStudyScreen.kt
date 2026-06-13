package com.lykimq_uyen.french_nationality.feature.question.presentation

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lykimq_uyen.french_nationality.core.ui.components.AppGradientBackground
import com.lykimq_uyen.french_nationality.core.ui.components.ErrorContent
import com.lykimq_uyen.french_nationality.core.ui.components.LoadingContent
import com.lykimq_uyen.french_nationality.feature.question.presentation.components.QuestionStudyContent

@Composable
fun QuestionStudyScreen(
    categoryId: String,
    subCategoryId: String,
    questionId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val application = LocalContext.current.applicationContext as Application
    val viewModel: QuestionStudyViewModel = viewModel(
        factory = QuestionStudyViewModelFactory(
            application = application,
            categoryId = categoryId,
            subCategoryId = subCategoryId,
            questionId = questionId,
        ),
    )
    val uiState by viewModel.uiState.collectAsState()

    QuestionStudyScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onPreviousClick = viewModel::goToPrevious,
        onNextClick = viewModel::goToNext,
        onJumpToNumber = viewModel::goToQuestionNumber,
        modifier = modifier,
    )
}

@Composable
private fun QuestionStudyScreenContent(
    uiState: QuestionStudyUiState,
    onBackClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onJumpToNumber: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        QuestionStudyUiState.Loading -> {
            AppGradientBackground(modifier = modifier) {
                LoadingContent(
                    message = "On prépare la question...",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        is QuestionStudyUiState.Success -> {
            QuestionStudyContent(
                category = uiState.category,
                currentItem = uiState.currentItem,
                totalQuestions = uiState.items.size,
                canGoPrevious = uiState.canGoPrevious,
                canGoNext = uiState.canGoNext,
                onBackClick = onBackClick,
                onPreviousClick = onPreviousClick,
                onNextClick = onNextClick,
                onJumpToNumber = onJumpToNumber,
                modifier = modifier,
            )
        }

        is QuestionStudyUiState.Error -> {
            AppGradientBackground(modifier = modifier) {
                ErrorContent(
                    title = "Oups",
                    message = uiState.message,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
