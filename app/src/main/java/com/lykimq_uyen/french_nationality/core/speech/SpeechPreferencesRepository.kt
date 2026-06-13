package com.lykimq_uyen.french_nationality.core.speech

interface SpeechPreferencesRepository {
    fun getVoiceGender(): VoiceGender

    fun saveVoiceGender(gender: VoiceGender)

    fun getSavedVoiceName(gender: VoiceGender): String?

    fun saveVoiceName(gender: VoiceGender, voiceName: String)
}
