package com.lykimq_uyen.french_nationality.feature.home.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lykimq_uyen.french_nationality.core.ui.components.AppGradientBackground
import com.lykimq_uyen.french_nationality.core.ui.components.ErrorContent
import com.lykimq_uyen.french_nationality.core.ui.components.LoadingContent
import com.lykimq_uyen.french_nationality.feature.home.presentation.components.CategoryListContent

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        HomeUiState.Loading -> {
            AppGradientBackground(modifier = modifier) {
                LoadingContent(
                    message = "On charge tes thèmes...",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        is HomeUiState.Success -> {
            CategoryListContent(
                categories = uiState.categories,
                modifier = modifier,
            )
        }

        is HomeUiState.Error -> {
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
