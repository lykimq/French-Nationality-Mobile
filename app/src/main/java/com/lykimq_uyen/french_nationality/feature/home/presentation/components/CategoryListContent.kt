package com.lykimq_uyen.french_nationality.feature.home.presentation.components

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
import androidx.compose.material.icons.outlined.Settings
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
import com.lykimq_uyen.french_nationality.ui.theme.DeepCyan
import com.lykimq_uyen.french_nationality.ui.theme.ElectricIndigo
import com.lykimq_uyen.french_nationality.ui.theme.PillShape
import com.lykimq_uyen.french_nationality.ui.theme.SkyBlue
import com.lykimq_uyen.french_nationality.ui.theme.VividViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListContent(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit = {},
    onSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    AppGradientBackground(modifier = modifier) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                HomeTopBar(
                    categoryCount = categories.size,
                    scrollBehavior = scrollBehavior,
                    onSettingsClick = onSettingsClick,
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
                    HomeSectionHeader(categoryCount = categories.size)
                }
                items(
                    items = categories,
                    key = { it.id },
                ) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    categoryCount: Int,
    scrollBehavior: androidx.compose.material3.TopAppBarScrollBehavior,
    onSettingsClick: () -> Unit,
) {
    LargeTopAppBar(
        title = {
            Column {
                Text(
                    text = "Naturalisation 🇫🇷",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        brush = titleGradient(),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                ThemeCountPill(count = categoryCount)
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Paramètres",
                )
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
private fun ThemeCountPill(
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
            text = "$count thèmes à explorer",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricIndigo,
        )
    }
}

@Composable
private fun HomeSectionHeader(
    categoryCount: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
    ) {
        Text(
            text = "Choisis ton thème ✨",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Tout est regroupé en $categoryCount catégories pour préparer l'entretien, chill.",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun titleGradient(): Brush {
    return Brush.linearGradient(
        colors = listOf(ElectricIndigo, VividViolet, DeepCyan),
    )
}
