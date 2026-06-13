package com.lykimq_uyen.french_nationality.core.speech

import android.speech.tts.Voice
import java.util.Locale

internal object FrenchVoiceSelector {

    private val hashGenderPattern = Regex(
        """#(?:female|male)(?:_\d+)?""",
        RegexOption.IGNORE_CASE,
    )
    private val maleVariantPattern = Regex("""male_(\d+)""", RegexOption.IGNORE_CASE)
    private val femaleVariantPattern = Regex("""female_(\d+)""", RegexOption.IGNORE_CASE)

    private val googleFemaleTokens = listOf("-frd", "#frd", "-frc", "#frc", "-fre", "#fre")
    private val googleMaleTokens = listOf("-frb", "#frb", "-frg", "#frg", "-frm", "#frm")
    private val carolineVoiceTokens = listOf("-frc", "#frc", "_frc")

    fun selectVoice(
        voices: List<Voice>,
        gender: VoiceGender,
    ): Voice? {
        if (voices.isEmpty()) {
            return null
        }

        return when (gender) {
            VoiceGender.MALE -> selectOnlineMaleVoice(voices)
            VoiceGender.FEMALE -> selectOnlineFemaleVoice(voices)
        }
    }

    fun selectOnlineFemaleVoice(voices: List<Voice>): Voice? {
        val carolineVoices = voices.filter { voice ->
            isCarolineVoice(voice) && classifyVoiceGender(voice) != VoiceGender.MALE
        }
        if (carolineVoices.isNotEmpty()) {
            val onlineCarolineVoices = carolineVoices.filter { voice ->
                voice.isNetworkConnectionRequired
            }
            val candidates = if (onlineCarolineVoices.isNotEmpty()) {
                onlineCarolineVoices
            } else {
                carolineVoices
            }
            return sortByNaturalness(candidates, VoiceGender.FEMALE).firstOrNull()
        }

        val femaleVoices = voices.filter { voice -> matchesGender(voice, VoiceGender.FEMALE) }
        if (femaleVoices.isEmpty()) {
            return null
        }

        val onlineFemaleVoices = femaleVoices.filter { voice -> voice.isNetworkConnectionRequired }
        val candidates = if (onlineFemaleVoices.isNotEmpty()) {
            onlineFemaleVoices
        } else {
            femaleVoices
        }

        return sortByNaturalness(candidates, VoiceGender.FEMALE).firstOrNull()
    }

    fun isCarolineVoice(voice: Voice): Boolean {
        val name = voice.name.lowercase(Locale.ROOT)
        return carolineVoiceTokens.any { token -> name.contains(token) } ||
            name.contains("caroline")
    }

    fun selectOnlineMaleVoice(voices: List<Voice>): Voice? {
        val maleVoices = voices.filter { voice -> matchesGender(voice, VoiceGender.MALE) }
        if (maleVoices.isEmpty()) {
            return null
        }

        val onlineMaleVoices = maleVoices.filter { voice -> voice.isNetworkConnectionRequired }
        val candidates = if (onlineMaleVoices.isNotEmpty()) {
            onlineMaleVoices
        } else {
            maleVoices
        }

        return sortByNaturalness(candidates, VoiceGender.MALE).firstOrNull()
    }

    fun sortByNaturalness(voices: List<Voice>, gender: VoiceGender): List<Voice> {
        return voices.sortedWith(
            compareByDescending<Voice> { naturalnessScore(it, gender) }
                .thenByDescending { it.quality }
                .thenBy { it.name },
        )
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

    fun naturalnessScore(voice: Voice, gender: VoiceGender): Int {
        val name = voice.name.lowercase(Locale.ROOT)
        var score = voice.quality * 10

        if (voice.isNetworkConnectionRequired) {
            score += 120
        }

        maleVariantPattern.find(name)?.groupValues?.getOrNull(1)?.toIntOrNull()?.let { variant ->
            score += variant * 20
        }
        femaleVariantPattern.find(name)?.groupValues?.getOrNull(1)?.toIntOrNull()?.let { variant ->
            score += variant * 20
        }

        when (gender) {
            VoiceGender.MALE -> {
                when {
                    name.contains("frb") -> score += 40
                    name.contains("frg") -> score += 25
                    name.contains("frm") -> score += 10
                }
            }
            VoiceGender.FEMALE -> {
                when {
                    name.contains("frc") -> score += 50
                    name.contains("frd") -> score += 30
                    name.contains("fre") -> score += 20
                }
            }
        }

        if (name.endsWith("-local") && !voice.isNetworkConnectionRequired) {
            score -= 15
        }

        return score
    }

    private fun isGoogleFemaleCode(name: String): Boolean {
        return googleFemaleTokens.any { token -> name.contains(token) }
    }

    private fun isGoogleMaleCode(name: String): Boolean {
        return googleMaleTokens.any { token -> name.contains(token) }
    }
}
