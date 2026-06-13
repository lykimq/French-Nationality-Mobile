package com.lykimq_uyen.french_nationality.feature.question.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.core.speech.rememberFrenchSpeechController
import com.lykimq_uyen.french_nationality.core.ui.components.AppGradientBackground
import com.lykimq_uyen.french_nationality.core.ui.modifier.horizontalSwipeNavigation
import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category
import com.lykimq_uyen.french_nationality.feature.home.presentation.mapper.CategoryVisual
import com.lykimq_uyen.french_nationality.feature.home.presentation.mapper.categoryVisual
import com.lykimq_uyen.french_nationality.feature.question.domain.model.buildJumpChunks
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionListItem
import com.lykimq_uyen.french_nationality.ui.theme.isDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionStudyContent(
    category: Category,
    currentItem: QuestionListItem,
    totalQuestions: Int,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onBackClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onJumpToNumber: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val visual = categoryVisual(category.iconKey)
    val speechController = rememberFrenchSpeechController()
    val isSpeechReady by speechController.isReady.collectAsState()
    var showJumpSheet by remember { mutableStateOf(false) }
    val chunks = remember(totalQuestions) {
        buildJumpChunks(totalQuestions)
    }

    LaunchedEffect(currentItem.question.id) {
        speechController.stop()
    }

    AppGradientBackground(modifier = modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                QuestionStudyTopBar(
                    currentNumber = currentItem.number,
                    totalQuestions = totalQuestions,
                    onBackClick = onBackClick,
                    onJumpClick = { showJumpSheet = true },
                )
            },
            bottomBar = {
                StudyNavigationBar(
                    currentNumber = currentItem.number,
                    totalQuestions = totalQuestions,
                    canGoPrevious = canGoPrevious,
                    canGoNext = canGoNext,
                    visual = visual,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    onJumpClick = { showJumpSheet = true },
                )
            },
        ) { innerPadding ->
            key(currentItem.question.id) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .horizontalSwipeNavigation(
                            onSwipeNext = onNextClick,
                            onSwipePrevious = onPreviousClick,
                            canSwipeNext = canGoNext,
                            canSwipePrevious = canGoPrevious,
                        )
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    QuestionNumberBadge(
                        number = currentItem.number,
                        visual = visual,
                    )
                    StudyTextCard(
                        title = "Question",
                        body = currentItem.question.question,
                        speakEnabled = isSpeechReady,
                        speakContentDescription = "Lire la question",
                        onSpeakClick = { speechController.speak(currentItem.question.question) },
                    )
                    StudyTextCard(
                        title = "Explication",
                        body = currentItem.question.explanation,
                        speakEnabled = isSpeechReady,
                        speakContentDescription = "Lire l'explication",
                        onSpeakClick = { speechController.speak(currentItem.question.explanation) },
                    )
                }
            }
        }
    }

    if (showJumpSheet) {
        JumpToQuestionSheet(
            totalQuestions = totalQuestions,
            chunks = chunks,
            onDismiss = { showJumpSheet = false },
            onJumpToNumber = { number ->
                showJumpSheet = false
                onJumpToNumber(number)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestionStudyTopBar(
    currentNumber: Int,
    totalQuestions: Int,
    onBackClick: () -> Unit,
    onJumpClick: () -> Unit,
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
            Text("Question $currentNumber / $totalQuestions")
        },
        actions = {
            TextButton(onClick = onJumpClick) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp),
                )
                Text("#")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

@Composable
private fun StudyNavigationBar(
    currentNumber: Int,
    totalQuestions: Int,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    visual: CategoryVisual,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onJumpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(studyBarColor())
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = onPreviousClick,
            enabled = canGoPrevious,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Précédente",
                tint = if (canGoPrevious) visual.gradientStart else MaterialTheme.colorScheme.outline,
            )
        }
        TextButton(onClick = onJumpClick) {
            Text(
                text = "$currentNumber / $totalQuestions",
                style = MaterialTheme.typography.labelLarge,
                color = visual.gradientStart,
            )
        }
        IconButton(
            onClick = onNextClick,
            enabled = canGoNext,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Suivante",
                tint = if (canGoNext) visual.gradientStart else MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
private fun QuestionNumberBadge(
    number: Int,
    visual: CategoryVisual,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(visual.gradientStart, visual.gradientEnd),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
        )
    }
}

@Composable
private fun StudyTextCard(
    title: String,
    body: String,
    speakEnabled: Boolean,
    speakContentDescription: String,
    onSpeakClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = cardSurfaceColor()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                IconButton(
                    onClick = onSpeakClick,
                    enabled = speakEnabled,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = speakContentDescription,
                        tint = if (speakEnabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                    )
                }
            }
            Text(
                text = body,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun cardSurfaceColor(): Color {
    return if (isDarkTheme()) {
        Color(0xFF252036).copy(alpha = 0.92f)
    } else {
        Color.White.copy(alpha = 0.94f)
    }
}

@Composable
private fun studyBarColor(): Color {
    return if (isDarkTheme()) {
        Color(0xFF1E1B2E).copy(alpha = 0.96f)
    } else {
        Color.White.copy(alpha = 0.96f)
    }
}
