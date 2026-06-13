package com.lykimq_uyen.french_nationality.core.speech

import android.speech.tts.Voice
import java.util.Locale

internal object FrenchVoiceSelector {

    private val hashGenderPattern = Regex(
        """#(?:female|male)(?:_\d+)?""",
        RegexOption.IGNORE_CASE,
    )

    private val googleFemaleTokens = listOf("-frd", "#frd", "-frc", "#frc", "-fre", "#fre")
    private val googleMaleTokens = listOf("-frb", "#frb", "-frg", "#frg", "-frm", "#frm")

    fun selectVoice(
        voices: List<Voice>,
        gender: VoiceGender,
        preferredVoiceName: String? = null,
    ): Voice? {
        if (voices.isEmpty()) {
            return null
        }

        if (!preferredVoiceName.isNullOrBlank()) {
            val preferred = voices.firstOrNull { voice ->
                voice.name == preferredVoiceName &&
                    classifyVoiceGender(voice) != oppositeGender(gender)
            }
            if (preferred != null && matchesGender(preferred, gender)) {
                return preferred
            }
        }

        val sortedByQuality = voices.sortedWith(voiceQualityComparator())
        val exactMatches = sortedByQuality.filter { voice -> matchesGender(voice, gender) }
        if (exactMatches.isNotEmpty()) {
            return exactMatches.first()
        }

        return null
    }

    fun matchesGender(voice: Voice, gender: VoiceGender): Boolean {
        return classifyVoiceGender(voice) == gender
    }

    fun classifyVoiceGender(voice: Voice): VoiceGender? {
        val name = voice.name.lowercase(Locale.ROOT)

        hashGenderPattern.find(name)?.let { match ->
            val token = match.value.lowercase(Locale.ROOT)
            return when {
                token.contains("female") -> VoiceGender.FEMALE
                token.contains("male") -> VoiceGender.MALE
                else -> null
            }
        }

        when {
            name.contains("female_") || name.contains("-female") || name.contains("_female") -> {
                return VoiceGender.FEMALE
            }
            name.contains("male_") || name.contains("-male") || name.contains("_male") -> {
                return VoiceGender.MALE
            }
        }

        for (feature in voice.features.orEmpty()) {
            val normalized = feature.lowercase(Locale.ROOT)
            when {
                normalized == "female" || normalized.endsWith(":female") -> return VoiceGender.FEMALE
                normalized == "male" || normalized.endsWith(":male") -> return VoiceGender.MALE
                normalized.contains("female") && !normalized.contains("male") -> return VoiceGender.FEMALE
                normalized.contains("male") && !normalized.contains("female") -> return VoiceGender.MALE
            }
        }

        when {
            isGoogleFemaleCode(name) -> return VoiceGender.FEMALE
            isGoogleMaleCode(name) -> return VoiceGender.MALE
        }

        when {
            name.contains("female") || name.contains("femme") || name.contains("woman") -> {
                return VoiceGender.FEMALE
            }
            name.contains("male") || name.contains("homme") -> return VoiceGender.MALE
        }

        val femaleNames = listOf(
            "claire", "julie", "amelie", "denise", "marie", "audrey", "celine", "caroline",
        )
        val maleNames = listOf(
            "pierre", "henri", "thomas", "bernard", "guy", "jacques", "nicolas", "paul",
        )

        when {
            femaleNames.any { hint -> name.contains(hint) } -> return VoiceGender.FEMALE
            maleNames.any { hint -> name.contains(hint) } -> return VoiceGender.MALE
        }

        return null
    }

    fun hasVoiceForGender(voices: List<Voice>, gender: VoiceGender): Boolean {
        return voices.any { voice -> matchesGender(voice, gender) }
    }

    private fun isGoogleFemaleCode(name: String): Boolean {
        return googleFemaleTokens.any { token -> name.contains(token) }
    }

    private fun isGoogleMaleCode(name: String): Boolean {
        return googleMaleTokens.any { token -> name.contains(token) }
    }

    private fun oppositeGender(gender: VoiceGender): VoiceGender {
        return when (gender) {
            VoiceGender.MALE -> VoiceGender.FEMALE
            VoiceGender.FEMALE -> VoiceGender.MALE
        }
    }

    private fun voiceQualityComparator(): Comparator<Voice> {
        return compareByDescending<Voice> { !it.isNetworkConnectionRequired }
            .thenByDescending { it.quality }
    }
}
