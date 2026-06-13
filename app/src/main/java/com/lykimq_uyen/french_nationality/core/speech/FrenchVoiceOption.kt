package com.lykimq_uyen.french_nationality.core.speech

import android.speech.tts.Voice

data class FrenchVoiceOption(
    val id: String,
    val label: String,
    val gender: VoiceGender?,
)

internal fun buildFrenchVoiceOptions(voices: List<Voice>): List<FrenchVoiceOption> {
    return voices.map { voice ->
        FrenchVoiceOption(
            id = voice.name,
            label = formatVoiceLabel(voice.name),
            gender = FrenchVoiceSelector.classifyVoiceGender(voice),
        )
    }
}

internal fun formatVoiceLabel(voiceName: String): String {
    val hashMatch = Regex("""#((?:female|male)(?:_\d+)?)""", RegexOption.IGNORE_CASE)
        .find(voiceName)
    if (hashMatch != null) {
        return hashMatch.groupValues[1]
            .replace("_", " ")
            .replaceFirstChar { char -> char.uppercaseChar() }
    }

    val googleMatch = Regex("""[-.#](fr[bcdegmr])(?:[-.#]|$)""", RegexOption.IGNORE_CASE)
        .find(voiceName.lowercase())
    if (googleMatch != null) {
        return "Voix ${googleMatch.groupValues[1]}"
    }

    return voiceName.substringAfterLast("-").ifBlank { voiceName }
}
