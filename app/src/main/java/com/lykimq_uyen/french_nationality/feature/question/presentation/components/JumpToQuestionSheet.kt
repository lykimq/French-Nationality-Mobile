package com.lykimq_uyen.french_nationality.feature.question.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionChunk
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionListItem
import com.lykimq_uyen.french_nationality.feature.question.domain.model.QuestionSearchConfig
import com.lykimq_uyen.french_nationality.feature.question.domain.model.searchQuestionsInSubCategory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JumpToQuestionSheet(
    totalQuestions: Int,
    chunks: List<QuestionChunk>,
    onDismiss: () -> Unit,
    onJumpToNumber: (Int) -> Unit,
    searchItems: List<QuestionListItem>? = null,
    onSelectQuestion: ((QuestionListItem) -> Unit)? = null,
    onSelectChunk: ((QuestionChunk) -> Unit)? = null,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchInput by remember { mutableStateOf("") }
    var numberInput by remember { mutableStateOf("") }
    var numberErrorMessage by remember { mutableStateOf<String?>(null) }
    val showSearch = searchItems != null && onSelectQuestion != null
    val chunkShortcutLabel = if (onSelectChunk != null) {
        "Afficher un bloc"
    } else {
        "Raccourcis par bloc"
    }
    val searchGroups = remember(searchInput, searchItems) {
        if (showSearch) {
            searchQuestionsInSubCategory(items = searchItems, query = searchInput)
        } else {
            emptyList()
        }
    }
    val totalSearchResults = searchGroups.sumOf { it.results.size + it.hiddenCount }
    val showSearchHint = searchInput.isBlank()
    val showSearchTooShort = searchInput.isNotBlank() &&
        searchInput.trim().length < QuestionSearchConfig.MIN_QUERY_LENGTH
    val showSearchEmpty = searchInput.trim().length >= QuestionSearchConfig.MIN_QUERY_LENGTH &&
        searchGroups.isEmpty()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 640.dp)
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "title") {
                Text(
                    text = if (showSearch) {
                        "Rechercher ou aller à #"
                    } else {
                        "Aller à une question"
                    },
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            if (showSearch) {
                item(key = "search_field") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Recherche dans cette section uniquement.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        OutlinedTextField(
                            value = searchInput,
                            onValueChange = { searchInput = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Mot ou phrase") },
                            singleLine = true,
                            placeholder = { Text("Ex. laïcité, Marianne, vote...") },
                        )
                        when {
                            showSearchHint -> {
                                Text(
                                    text = "Minimum ${QuestionSearchConfig.MIN_QUERY_LENGTH} caractères.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            showSearchTooShort -> {
                                Text(
                                    text = "Tape au moins ${QuestionSearchConfig.MIN_QUERY_LENGTH} caractères.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            showSearchEmpty -> {
                                Text(
                                    text = "Aucun résultat dans cette section.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            else -> {
                                Text(
                                    text = "$totalSearchResults résultat${if (totalSearchResults > 1) "s" else ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }

                searchGroups.forEach { group ->
                    item(key = "group_header_${group.matchType.name}") {
                        SearchResultGroupHeader(
                            label = group.matchType.label,
                            count = group.results.size + group.hiddenCount,
                        )
                    }
                    items(
                        items = group.results,
                        key = { result -> "search_${group.matchType.name}_${result.item.question.id}" },
                    ) { result ->
                        SearchResultRow(
                            item = result.item,
                            matchedInQuestion = result.matchedInQuestion,
                            matchedInExplanation = result.matchedInExplanation,
                            onClick = { onSelectQuestion?.invoke(result.item) },
                        )
                    }
                    if (group.hiddenCount > 0) {
                        item(key = "group_more_${group.matchType.name}") {
                            Text(
                                text = "+ ${group.hiddenCount} autre${if (group.hiddenCount > 1) "s" else ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 4.dp),
                            )
                        }
                    }
                }

                item(key = "divider") {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }

                item(key = "number_title") {
                    Text(
                        text = "Aller à une question",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                item(key = "number_hint") {
                    Text(
                        text = "Entre un numéro entre 1 et $totalQuestions.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                item(key = "number_hint_top") {
                    Text(
                        text = "Entre un numéro entre 1 et $totalQuestions.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            item(key = "number_field") {
                OutlinedTextField(
                    value = numberInput,
                    onValueChange = {
                        numberInput = it.filter { char -> char.isDigit() }.take(4)
                        numberErrorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Numéro de question") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = numberErrorMessage != null,
                    supportingText = numberErrorMessage?.let { message ->
                        { Text(message) }
                    },
                )
            }

            if (chunks.isNotEmpty()) {
                item(key = "chunk_label") {
                    Text(
                        text = chunkShortcutLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                item(key = "chunk_chips") {
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
            }

            item(key = "open_button") {
                Button(
                    onClick = {
                        val number = numberInput.toIntOrNull()
                        when {
                            number == null -> numberErrorMessage = "Numéro invalide"
                            number !in 1..totalQuestions -> {
                                numberErrorMessage = "Choisis entre 1 et $totalQuestions"
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
}

@Composable
private fun SearchResultGroupHeader(
    label: String,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SearchResultRow(
    item: QuestionListItem,
    matchedInQuestion: Boolean,
    matchedInExplanation: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val matchLocation = when {
        matchedInQuestion && matchedInExplanation -> "Question et réponse"
        matchedInQuestion -> "Question"
        matchedInExplanation -> "Réponse"
        else -> ""
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "#${item.number}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            if (matchLocation.isNotEmpty()) {
                Text(
                    text = matchLocation,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Text(
            text = item.question.question,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
