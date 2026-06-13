package com.lykimq_uyen.french_nationality.feature.question.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.core.ui.modifier.subtlePressScale
import com.lykimq_uyen.french_nationality.feature.home.presentation.mapper.CategoryVisual
import com.lykimq_uyen.french_nationality.ui.theme.isDarkTheme

@Composable
fun ResumeStudyCard(
    questionNumber: Int,
    visual: CategoryVisual,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .subtlePressScale(onClick = onClick)
            .shadow(
                elevation = 12.dp,
                shape = MaterialTheme.shapes.medium,
                ambientColor = visual.gradientStart.copy(alpha = 0.22f),
                spotColor = visual.gradientEnd.copy(alpha = 0.28f),
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = cardSurfaceColor()),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            visual.gradientStart.copy(alpha = 0.12f),
                            visual.gradientEnd.copy(alpha = 0.18f),
                        ),
                    ),
                )
                .padding(horizontal = 18.dp, vertical = 18.dp),
        ) {
            Text(
                text = "Reprendre ou tu t'es arrêté",
                style = MaterialTheme.typography.labelLarge,
                color = visual.gradientStart,
            )
            Text(
                text = "Question $questionNumber",
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.padding(top = 8.dp),
                tint = visual.gradientStart,
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
