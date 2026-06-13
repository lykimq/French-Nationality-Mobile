package com.lykimq_uyen.french_nationality.feature.question.presentation.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.core.ui.components.AppGradientBackground
import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category
import com.lykimq_uyen.french_nationality.feature.home.presentation.mapper.categoryVisual
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionChunk
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionListItem
import com.lykimq_uyen.french_nationality.feature.question.domain.model.chunkIndexForQuestionNumber
import com.lykimq_uyen.french_nationality.feature.question.domain.model.findByQuestionId
import com.lykimq_uyen.french_nationality.feature.subcategory.domain.model.SubCategory
import com.lykimq_uyen.french_nationality.ui.theme.ElectricIndigo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListContent(
    category: Category,
    subCategory: SubCategory,
    items: List<QuestionListItem>,
    chunks: List<QuestionChunk>,
    lastQuestionId: String?,
    isLargeList: Boolean,
    onBackClick: () -> Unit,
    onQuestionClick: (QuestionListItem) -> Unit,
    onResumeClick: (QuestionListItem) -> Unit,
    onJumpToNumber: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val visual = categoryVisual(category.iconKey)
    val listState = rememberLazyListState()
    var showJumpSheet by remember { mutableStateOf(false) }
    val resumeItem = remember(lastQuestionId, items) {
        lastQuestionId?.let { items.findByQuestionId(it) }
    }
    val progress = remember(resumeItem, items) {
        if (items.isEmpty()) {
            0f
        } else {
            (resumeItem?.number ?: 0).toFloat() / items.size.toFloat()
        }
    }
    var selectedChunkIndex by remember(chunks, resumeItem) {
        mutableIntStateOf(
            if (isLargeList && resumeItem != null) {
                chunkIndexForQuestionNumber(chunks, resumeItem.number)
            } else {
                0
            },
        )
    }
    val visibleItems = remember(isLargeList, items, chunks, selectedChunkIndex) {
        if (isLargeList && chunks.isNotEmpty()) {
            chunks[selectedChunkIndex.coerceIn(0, chunks.lastIndex)].items
        } else {
            items
        }
    }

    LaunchedEffect(selectedChunkIndex) {
        listState.scrollToItem(0)
    }

    AppGradientBackground(modifier = modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                QuestionListTopBar(
                    subCategoryTitle = subCategory.title,
                    onBackClick = onBackClick,
                    onJumpClick = { showJumpSheet = true },
                    showJumpAction = items.size > 1,
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
                    top = 8.dp,
                    bottom = 32.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item(key = "progress") {
                    StudyProgressHeader(
                        currentNumber = resumeItem?.number ?: 0,
                        totalQuestions = items.size,
                        progress = progress,
                    )
                }

                if (resumeItem != null) {
                    item(key = "resume") {
                        ResumeStudyCard(
                            questionNumber = resumeItem.number,
                            visual = visual,
                            onClick = { onResumeClick(resumeItem) },
                        )
                    }
                }

                item(key = "section_header") {
                    QuestionListSectionHeader(
                        totalQuestions = items.size,
                        isLargeList = isLargeList,
                    )
                }

                if (isLargeList && chunks.isNotEmpty()) {
                    item(key = "chunk_nav_$selectedChunkIndex") {
                        QuestionChunkNavigator(
                            chunk = chunks[selectedChunkIndex],
                            chunkIndex = selectedChunkIndex,
                            totalChunks = chunks.size,
                            onPreviousChunk = {
                                if (selectedChunkIndex > 0) {
                                    selectedChunkIndex -= 1
                                }
                            },
                            onNextChunk = {
                                if (selectedChunkIndex < chunks.lastIndex) {
                                    selectedChunkIndex += 1
                                }
                            },
                        )
                    }
                }

                items(
                    items = visibleItems,
                    key = { it.question.id },
                ) { item ->
                    QuestionRowCard(
                        item = item,
                        visual = visual,
                        onClick = { onQuestionClick(item) },
                    )
                }
            }
        }
    }

    if (showJumpSheet) {
        JumpToQuestionSheet(
            totalQuestions = items.size,
            chunks = chunks,
            onDismiss = { showJumpSheet = false },
            onJumpToNumber = { number ->
                showJumpSheet = false
                onJumpToNumber(number)
            },
            onSelectChunk = if (isLargeList && chunks.isNotEmpty()) {
                { chunk ->
                    selectedChunkIndex = chunks.indexOfFirst {
                        it.startNumber == chunk.startNumber
                    }.coerceAtLeast(0)
                    showJumpSheet = false
                }
            } else {
                null
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestionListTopBar(
    subCategoryTitle: String,
    onBackClick: () -> Unit,
    onJumpClick: () -> Unit,
    showJumpAction: Boolean,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                )
            }
        },
        title = {
            Text(
                text = subCategoryTitle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        actions = {
            if (showJumpAction) {
                TextButton(onClick = onJumpClick) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp),
                    )
                    Text("Aller à #")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

@Composable
private fun StudyProgressHeader(
    currentNumber: Int,
    totalQuestions: Int,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = if (currentNumber > 0) {
                "Progression : question $currentNumber / $totalQuestions"
            } else {
                "$totalQuestions questions à parcourir"
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            color = ElectricIndigo,
            trackColor = ElectricIndigo.copy(alpha = 0.18f),
        )
    }
}

@Composable
private fun QuestionListSectionHeader(
    totalQuestions: Int,
    isLargeList: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Liste des questions",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = if (isLargeList) {
                "$totalQuestions questions en blocs de 25. Affiche un bloc, puis passe au suivant."
            } else {
                "Choisis une question pour commencer ou reprendre."
            },
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
