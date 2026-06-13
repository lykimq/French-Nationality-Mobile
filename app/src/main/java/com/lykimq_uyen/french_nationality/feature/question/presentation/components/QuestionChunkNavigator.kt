package com.lykimq_uyen.french_nationality.feature.question.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionChunk
import com.lykimq_uyen.french_nationality.ui.theme.ElectricIndigo
import com.lykimq_uyen.french_nationality.ui.theme.PillShape

@Composable
fun QuestionChunkNavigator(
    chunk: QuestionChunk,
    chunkIndex: Int,
    totalChunks: Int,
    onPreviousChunk: () -> Unit,
    onNextChunk: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        ElectricIndigo.copy(alpha = 0.12f),
                        ElectricIndigo.copy(alpha = 0.04f),
                    ),
                ),
                shape = PillShape,
            )
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = onPreviousChunk,
            enabled = chunkIndex > 0,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Bloc précédent",
                tint = if (chunkIndex > 0) {
                    ElectricIndigo
                } else {
                    MaterialTheme.colorScheme.outline
                },
            )
        }
        Text(
            text = "Questions ${chunk.startNumber}-${chunk.endNumber} (${
                chunkIndex + 1
            }/$totalChunks)",
            style = MaterialTheme.typography.labelLarge,
            color = ElectricIndigo,
        )
        IconButton(
            onClick = onNextChunk,
            enabled = chunkIndex < totalChunks - 1,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Bloc suivant",
                tint = if (chunkIndex < totalChunks - 1) {
                    ElectricIndigo
                } else {
                    MaterialTheme.colorScheme.outline
                },
            )
        }
    }
}
