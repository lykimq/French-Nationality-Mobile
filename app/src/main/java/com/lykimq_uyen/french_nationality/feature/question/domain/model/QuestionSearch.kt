package com.lykimq_uyen.french_nationality.feature.question.domain.model

import java.text.Normalizer

enum class QuestionSearchMatchType(val label: String, val sortOrder: Int) {
    EXACT("Correspondance exacte", 0),
    PHRASE("Phrase trouvée", 1),
    ALL_WORDS("Tous les mots", 2),
}

data class QuestionSearchResult(
    val item: QuestionListItem,
    val matchType: QuestionSearchMatchType,
    val matchedInQuestion: Boolean,
    val matchedInExplanation: Boolean,
)

data class QuestionSearchResultGroup(
    val matchType: QuestionSearchMatchType,
    val results: List<QuestionSearchResult>,
    val hiddenCount: Int = 0,
)

object QuestionSearchConfig {
    const val MIN_QUERY_LENGTH = 2
    const val MAX_RESULTS_PER_GROUP = 15
}

private val SEARCH_STOP_WORDS = setOf(
    "a",
    "au",
    "aux",
    "de",
    "des",
    "du",
    "en",
    "et",
    "la",
    "le",
    "les",
    "ou",
    "un",
    "une",
)

fun searchQuestionsInSubCategory(
    items: List<QuestionListItem>,
    query: String,
    maxResultsPerGroup: Int = QuestionSearchConfig.MAX_RESULTS_PER_GROUP,
): List<QuestionSearchResultGroup> {
    val normalizedQuery = normalizeSearchText(query)
    if (normalizedQuery.length < QuestionSearchConfig.MIN_QUERY_LENGTH) {
        return emptyList()
    }

    val results = items.mapNotNull { item ->
        classifySearchMatch(
            question = item.question.question,
            explanation = item.question.explanation,
            normalizedQuery = normalizedQuery,
        )?.let { match ->
            QuestionSearchResult(
                item = item,
                matchType = match.matchType,
                matchedInQuestion = match.matchedInQuestion,
                matchedInExplanation = match.matchedInExplanation,
            )
        }
    }.sortedWith(
        compareBy<QuestionSearchResult> { it.matchType.sortOrder }
            .thenByDescending { it.matchedInQuestion }
            .thenBy { it.item.number },
    )

    return QuestionSearchMatchType.entries.mapNotNull { matchType ->
        val groupResults = results.filter { it.matchType == matchType }
        if (groupResults.isEmpty()) {
            null
        } else {
            val visible = groupResults.take(maxResultsPerGroup)
            QuestionSearchResultGroup(
                matchType = matchType,
                results = visible,
                hiddenCount = (groupResults.size - visible.size).coerceAtLeast(0),
            )
        }
    }
}

private data class SearchMatchClassification(
    val matchType: QuestionSearchMatchType,
    val matchedInQuestion: Boolean,
    val matchedInExplanation: Boolean,
)

private fun classifySearchMatch(
    question: String,
    explanation: String,
    normalizedQuery: String,
): SearchMatchClassification? {
    val normalizedQuestion = normalizeSearchText(question)
    val normalizedExplanation = normalizeSearchText(explanation)

    if (normalizedQuestion == normalizedQuery || normalizedExplanation == normalizedQuery) {
        return SearchMatchClassification(
            matchType = QuestionSearchMatchType.EXACT,
            matchedInQuestion = normalizedQuestion == normalizedQuery,
            matchedInExplanation = normalizedExplanation == normalizedQuery,
        )
    }

    val phraseInQuestion = containsWholePhrase(normalizedQuestion, normalizedQuery)
    val phraseInExplanation = containsWholePhrase(normalizedExplanation, normalizedQuery)
    if (phraseInQuestion || phraseInExplanation) {
        return SearchMatchClassification(
            matchType = QuestionSearchMatchType.PHRASE,
            matchedInQuestion = phraseInQuestion,
            matchedInExplanation = phraseInExplanation,
        )
    }

    val tokens = meaningfulSearchTokens(normalizedQuery)
    if (tokens.isEmpty()) {
        return null
    }

    if (tokens.size == 1) {
        return null
    }

    val allWordsInQuestion = containsAllWholeWords(normalizedQuestion, tokens)
    val allWordsInExplanation = containsAllWholeWords(normalizedExplanation, tokens)
    if (allWordsInQuestion || allWordsInExplanation) {
        return SearchMatchClassification(
            matchType = QuestionSearchMatchType.ALL_WORDS,
            matchedInQuestion = allWordsInQuestion,
            matchedInExplanation = allWordsInExplanation,
        )
    }

    return null
}

internal fun normalizeSearchText(text: String): String {
    return removeAccents(text.trim().lowercase())
        .replace(Regex("\\s+"), " ")
}

private fun removeAccents(text: String): String {
    return Normalizer.normalize(text, Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "")
}

private fun tokenizeSearchQuery(normalizedQuery: String): List<String> {
    return normalizedQuery
        .split(Regex("[\\s,.;:!?\"'«»()\\[\\]-]+"))
        .filter { it.length >= 2 }
}

private fun meaningfulSearchTokens(normalizedQuery: String): List<String> {
    val tokens = tokenizeSearchQuery(normalizedQuery)
    if (tokens.isEmpty()) {
        return emptyList()
    }
    val withoutStopWords = tokens.filter { it !in SEARCH_STOP_WORDS }
    return withoutStopWords.ifEmpty { tokens }
}

private fun containsWholeWord(text: String, word: String): Boolean {
    if (word.isBlank()) {
        return false
    }
    val pattern = Regex("(?<![a-z0-9])${Regex.escape(word)}(?![a-z0-9])")
    return pattern.containsMatchIn(text)
}

private fun containsWholePhrase(text: String, phrase: String): Boolean {
    val words = tokenizeSearchQuery(phrase)
    if (words.isEmpty()) {
        return false
    }
    if (words.size == 1) {
        return containsWholeWord(text, words.first())
    }
    val separator = "[\\s,.;:!?\"'«»()\\[\\]-]+"
    val pattern = words.joinToString(separator = separator) { Regex.escape(it) }
    return Regex("(?<![a-z0-9])$pattern(?![a-z0-9])").containsMatchIn(text)
}

private fun containsAllWholeWords(text: String, words: List<String>): Boolean {
    return words.all { word -> containsWholeWord(text, word) }
}
