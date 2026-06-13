package com.lykimq_uyen.french_nationality.feature.question.presentation

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lykimq_uyen.french_nationality.core.ui.components.AppGradientBackground
import com.lykimq_uyen.french_nationality.core.ui.components.ErrorContent
import com.lykimq_uyen.french_nationality.core.ui.components.LoadingContent
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionListItem
import com.lykimq_uyen.french_nationality.feature.question.domain.model.findByNumber
import com.lykimq_uyen.french_nationality.feature.question.presentation.components.QuestionListContent

@Composable
fun QuestionListScreen(
    categoryId: String,
    subCategoryId: String,
    onBackClick: () -> Unit,
    onQuestionClick: (QuestionListItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val application = LocalContext.current.applicationContext as Application
    val viewModel: QuestionListViewModel = viewModel(
        factory = QuestionListViewModelFactory(
            application = application,
            categoryId = categoryId,
            subCategoryId = subCategoryId,
        ),
    )
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshProgress()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    QuestionListScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onQuestionClick = onQuestionClick,
        modifier = modifier,
    )
}

@Composable
private fun QuestionListScreenContent(
    uiState: QuestionListUiState,
    onBackClick: () -> Unit,
    onQuestionClick: (QuestionListItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        QuestionListUiState.Loading -> {
            AppGradientBackground(modifier = modifier) {
                LoadingContent(
                    message = "On charge les questions...",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        is QuestionListUiState.Success -> {
            QuestionListContent(
                category = uiState.category,
                subCategory = uiState.subCategory,
                items = uiState.items,
                chunks = uiState.chunks,
                lastQuestionId = uiState.lastQuestionId,
                isLargeList = uiState.isLargeList,
                onBackClick = onBackClick,
                onQuestionClick = onQuestionClick,
                onJumpToNumber = { number ->
                    uiState.items.findByNumber(number)?.let(onQuestionClick)
                },
                modifier = modifier,
            )
        }

        is QuestionListUiState.Error -> {
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
