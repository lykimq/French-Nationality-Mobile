package com.lykimq_uyen.french_nationality.feature.question.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionChunk

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JumpToQuestionSheet(
    totalQuestions: Int,
    chunks: List<QuestionChunk>,
    onDismiss: () -> Unit,
    onJumpToNumber: (Int) -> Unit,
    onSelectChunk: ((QuestionChunk) -> Unit)? = null,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var input by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val chunkShortcutLabel = if (onSelectChunk != null) {
        "Afficher un bloc"
    } else {
        "Raccourcis par bloc"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Aller à une question",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Entre un numéro entre 1 et $totalQuestions.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = input,
                onValueChange = {
                    input = it.filter { char -> char.isDigit() }.take(4)
                    errorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Numéro de question") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = errorMessage != null,
                supportingText = errorMessage?.let { message ->
                    { Text(message) }
                },
            )
            if (chunks.isNotEmpty()) {
                Text(
                    text = chunkShortcutLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    chunks.forEach { chunk ->
                        SuggestionChip(
                            onClick = {
                                if (onSelectChunk != null) {
                                    onSelectChunk(chunk)
                                } else {
                                    onJumpToNumber(chunk.startNumber)
                                }
                            },
                            label = {
                                Text("${chunk.startNumber}-${chunk.endNumber}")
                            },
                        )
                    }
                }
            }
            Button(
                onClick = {
                    val number = input.toIntOrNull()
                    when {
                        number == null -> errorMessage = "Numéro invalide"
                        number !in 1..totalQuestions -> {
                            errorMessage = "Choisis entre 1 et $totalQuestions"
                        }
                        else -> onJumpToNumber(number)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Ouvrir la question")
            }
        }
    }
}
