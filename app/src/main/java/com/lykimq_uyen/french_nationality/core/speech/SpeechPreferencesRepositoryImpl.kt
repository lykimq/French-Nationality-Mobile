package com.lykimq_uyen.french_nationality.core.speech

import android.content.Context

class SpeechPreferencesRepositoryImpl(
    context: Context,
) : SpeechPreferencesRepository {

    private val preferences = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getVoiceGender(): VoiceGender {
        return when (preferences.getString(KEY_VOICE_GENDER, VoiceGender.MALE.name)) {
            VoiceGender.FEMALE.name -> VoiceGender.FEMALE
            else -> VoiceGender.MALE
        }
    }

    override fun saveVoiceGender(gender: VoiceGender) {
        preferences.edit()
            .putString(KEY_VOICE_GENDER, gender.name)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "speech_preferences"
        private const val KEY_VOICE_GENDER = "voice_gender"
    }
}
