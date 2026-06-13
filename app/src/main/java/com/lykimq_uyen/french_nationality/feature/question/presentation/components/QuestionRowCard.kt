package com.lykimq_uyen.french_nationality.feature.question.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.core.ui.modifier.subtlePressScale
import com.lykimq_uyen.french_nationality.feature.home.presentation.mapper.CategoryVisual
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionListItem
import com.lykimq_uyen.french_nationality.ui.theme.isDarkTheme

@Composable
fun QuestionRowCard(
    item: QuestionListItem,
    visual: CategoryVisual,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .subtlePressScale(onClick = onClick)
            .shadow(
                elevation = 6.dp,
                shape = MaterialTheme.shapes.small,
                ambientColor = visual.gradientStart.copy(alpha = 0.12f),
                spotColor = visual.gradientEnd.copy(alpha = 0.16f),
            ),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = cardSurfaceColor()),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
        ) {
            QuestionNumberBadge(
                number = item.number,
                visual = visual,
            )
            Text(
                text = item.question.question,
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
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
            .size(32.dp)
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
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
        )
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
