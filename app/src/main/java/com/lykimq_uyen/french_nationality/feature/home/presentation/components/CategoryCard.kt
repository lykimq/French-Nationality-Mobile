package com.lykimq_uyen.french_nationality.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.lykimq_uyen.french_nationality.feature.home.domain.model.Category
import com.lykimq_uyen.french_nationality.feature.home.presentation.mapper.CategoryVisual
import com.lykimq_uyen.french_nationality.feature.home.presentation.mapper.categoryVisual
import com.lykimq_uyen.french_nationality.ui.theme.isDarkTheme

@Composable
fun CategoryCard(
    category: Category,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val visual = categoryVisual(category.iconKey)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .subtlePressScale(onClick = onClick)
            .shadow(
                elevation = 10.dp,
                shape = MaterialTheme.shapes.medium,
                ambientColor = visual.gradientStart.copy(alpha = 0.18f),
                spotColor = visual.gradientEnd.copy(alpha = 0.22f),
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = cardSurfaceColor(),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GradientIconBadge(visual = visual)
            CategoryTexts(
                title = category.title,
                description = category.description,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            )
            GradientChevron(visual = visual)
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
private fun GradientIconBadge(
    visual: CategoryVisual,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(visual.gradientStart, visual.gradientEnd),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = visual.icon,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = Color.White,
        )
    }
}

@Composable
private fun GradientChevron(
    visual: CategoryVisual,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        visual.gradientStart.copy(alpha = 0.15f),
                        visual.gradientEnd.copy(alpha = 0.25f),
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = visual.gradientStart,
        )
    }
}

@Composable
private fun CategoryTexts(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (description.isNotBlank()) {
            Text(
                text = description,
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
