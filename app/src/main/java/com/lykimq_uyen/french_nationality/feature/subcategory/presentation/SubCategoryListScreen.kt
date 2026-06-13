package com.lykimq_uyen.french_nationality.feature.subcategory.presentation

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
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory
import com.lykimq_uyen.french_nationality.feature.subcategory.presentation.components.SubCategoryListContent

@Composable
fun SubCategoryListScreen(
    categoryId: String,
    onBackClick: () -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val application = LocalContext.current.applicationContext as Application
    val viewModel: SubCategoryViewModel = viewModel(
        factory = SubCategoryViewModelFactory(
            application = application,
            categoryId = categoryId,
        ),
    )
    val uiState by viewModel.uiState.collectAsState()

    SubCategoryScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onSubCategoryClick = onSubCategoryClick,
        modifier = modifier,
    )
}

@Composable
private fun SubCategoryScreenContent(
    uiState: SubCategoryUiState,
    onBackClick: () -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        SubCategoryUiState.Loading -> {
            AppGradientBackground(modifier = modifier) {
                LoadingContent(
                    message = "On charge les sections...",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        is SubCategoryUiState.Success -> {
            SubCategoryListContent(
                category = uiState.category,
                subCategories = uiState.subCategories,
                onBackClick = onBackClick,
                onSubCategoryClick = onSubCategoryClick,
                modifier = modifier,
            )
        }

        is SubCategoryUiState.Error -> {
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
