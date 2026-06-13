package com.lykimq_uyen.french_nationality.core.speech

import android.speech.tts.Voice

internal fun formatActiveVoiceDescription(voice: Voice): String {
    val label = formatVoiceLabel(voice)
    val subtitle = formatVoiceSubtitle(voice)
    return if (subtitle.isNullOrBlank()) {
        label
    } else {
        "$label · $subtitle"
    }
}

internal fun formatVoiceLabel(voice: Voice): String {
    val name = voice.name.lowercase()
    val friendlyName = when {
        name.contains("frb") -> "Bernard"
        name.contains("frg") -> "Guy"
        name.contains("frd") -> "Denise"
        name.contains("frc") -> "Caroline"
        name.contains("fre") -> "Eva"
        else -> null
    }
    if (friendlyName != null) {
        return friendlyName
    }

    val hashMatch = Regex("""#((?:female|male)(?:_\d+)?)""", RegexOption.IGNORE_CASE)
        .find(voice.name)
    if (hashMatch != null) {
        return hashMatch.groupValues[1]
            .replace("_", " ")
            .replaceFirstChar { char -> char.uppercaseChar() }
    }

    val googleMatch = Regex("""[-.#](fr[bcdegmr])(?:[-.#]|$)""", RegexOption.IGNORE_CASE)
        .find(name)
    if (googleMatch != null) {
        return "Voix ${googleMatch.groupValues[1]}"
    }

    return voice.name.substringAfterLast("-").ifBlank { voice.name }
}

internal fun formatVoiceSubtitle(voice: Voice): String? {
    return when {
        voice.isNetworkConnectionRequired -> "En ligne, plus naturelle"
        voice.name.lowercase().endsWith("-local") -> "Hors ligne"
        else -> null
    }
}
