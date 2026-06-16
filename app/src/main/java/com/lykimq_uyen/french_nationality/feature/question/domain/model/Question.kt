package com.lykimq_uyen.french_nationality.feature.question.domain.model

data class Question(
    val id: String,
    val question: String,
    val explanation: String,
)

data class QuestionListItem(
    val question: Question,
    val number: Int,
)

data class QuestionChunk(
    val startNumber: Int,
    val endNumber: Int,
    val items: List<QuestionListItem>,
)

object QuestionListConfig {
    const val LARGE_LIST_THRESHOLD = 30
    const val CHUNK_SIZE = 25
}

fun List<Question>.toNumberedItems(): List<QuestionListItem> {
    return mapIndexed { index, question ->
        QuestionListItem(
            question = question,
            number = index + 1,
        )
    }
}

fun List<QuestionListItem>.toChunks(chunkSize: Int): List<QuestionChunk> {
    if (isEmpty()) {
        return emptyList()
    }
    return chunked(chunkSize).map { chunkItems ->
        QuestionChunk(
            startNumber = chunkItems.first().number,
            endNumber = chunkItems.last().number,
            items = chunkItems,
        )
    }
}

fun List<QuestionListItem>.findByNumber(number: Int): QuestionListItem? {
    return getOrNull(number - 1)
}

fun List<QuestionListItem>.findByQuestionId(questionId: String): QuestionListItem? {
    return firstOrNull { it.question.id == questionId }
}

fun resolveSavedQuestionId(
    items: List<QuestionListItem>,
    savedQuestionId: String?,
    onInvalidSavedId: (() -> Unit)? = null,
): String? {
    if (savedQuestionId == null) {
        return null
    }
    return if (items.any { it.question.id == savedQuestionId }) {
        savedQuestionId
    } else {
        onInvalidSavedId?.invoke()
        null
    }
}

fun buildJumpChunks(
    totalQuestions: Int,
    chunkSize: Int = QuestionListConfig.CHUNK_SIZE,
): List<QuestionChunk> {
    if (totalQuestions <= QuestionListConfig.LARGE_LIST_THRESHOLD) {
        return emptyList()
    }
    val chunks = mutableListOf<QuestionChunk>()
    var start = 1
    while (start <= totalQuestions) {
        val end = minOf(start + chunkSize - 1, totalQuestions)
        chunks.add(
            QuestionChunk(
                startNumber = start,
                endNumber = end,
                items = emptyList(),
            ),
        )
        start = end + 1
    }
    return chunks
}

fun chunkIndexForQuestionNumber(chunks: List<QuestionChunk>, questionNumber: Int): Int {
    if (chunks.isEmpty()) {
        return 0
    }
    val index = chunks.indexOfFirst { questionNumber in it.startNumber..it.endNumber }
    return if (index >= 0) index else 0
}
