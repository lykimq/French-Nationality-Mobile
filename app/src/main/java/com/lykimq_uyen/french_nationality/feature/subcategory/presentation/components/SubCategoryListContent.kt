package com.lykimq_uyen.french_nationality.feature.subcategory.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.core.ui.components.AppGradientBackground
import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category
import com.lykimq_uyen.french_nationality.feature.home.presentation.mapper.CategoryVisual
import com.lykimq_uyen.french_nationality.feature.home.presentation.mapper.categoryVisual
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory
import com.lykimq_uyen.french_nationality.ui.theme.DeepCyan
import com.lykimq_uyen.french_nationality.ui.theme.ElectricIndigo
import com.lykimq_uyen.french_nationality.ui.theme.PillShape
import com.lykimq_uyen.french_nationality.ui.theme.SkyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCategoryListContent(
    category: Category,
    subCategories: List<SubCategory>,
    onBackClick: () -> Unit,
    onSubCategoryClick: (SubCategory) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val visual = categoryVisual(category.iconKey)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    AppGradientBackground(modifier = modifier) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                SubCategoryTopBar(
                    category = category,
                    visual = visual,
                    subCategoryCount = subCategories.size,
                    onBackClick = onBackClick,
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 4.dp,
                    bottom = 32.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item(key = "section_header") {
                    SubCategorySectionHeader(
                        subCategoryCount = subCategories.size,
                        modifier = Modifier.animateItem(),
                    )
                }
                items(
                    items = subCategories,
                    key = { it.id },
                ) { subCategory ->
                    SubCategoryCard(
                        subCategory = subCategory,
                        visual = visual,
                        onClick = { onSubCategoryClick(subCategory) },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubCategoryTopBar(
    category: Category,
    visual: CategoryVisual,
    subCategoryCount: Int,
    onBackClick: () -> Unit,
    scrollBehavior: androidx.compose.material3.TopAppBarScrollBehavior,
) {
    LargeTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        title = {
            Column {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        brush = titleGradient(visual),
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                SectionCountPill(count = subCategoryCount)
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
    )
}

@Composable
private fun SectionCountPill(
    count: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(top = 10.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        ElectricIndigo.copy(alpha = 0.14f),
                        SkyBlue.copy(alpha = 0.18f),
                    ),
                ),
                shape = PillShape,
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = if (count == 1) "1 section" else "$count sections",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricIndigo,
        )
    }
}

@Composable
private fun SubCategorySectionHeader(
    subCategoryCount: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
    ) {
        Text(
            text = "Choisis une section",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "$subCategoryCount sous-thèmes pour avancer étape par étape.",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun titleGradient(visual: CategoryVisual): Brush {
    return Brush.linearGradient(
        colors = listOf(visual.gradientStart, visual.gradientEnd, DeepCyan),
    )
}
