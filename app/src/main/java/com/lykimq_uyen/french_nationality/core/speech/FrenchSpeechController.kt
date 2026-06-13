package com.lykimq_uyen.french_nationality.core.speech

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

class FrenchSpeechController(
    context: Context,
    private val speechPreferencesRepository: SpeechPreferencesRepository,
) {
    private val appContext = context.applicationContext
    private var textToSpeech: TextToSpeech? = null
    private val isInitialized = AtomicBoolean(false)
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _voiceGender = MutableStateFlow(speechPreferencesRepository.getVoiceGender())
    val voiceGender: StateFlow<VoiceGender> = _voiceGender.asStateFlow()

    private val _activeVoiceLabel = MutableStateFlow<String?>(null)
    val activeVoiceLabel: StateFlow<String?> = _activeVoiceLabel.asStateFlow()

    private val _isGenderVoiceAvailable = MutableStateFlow(true)
    val isGenderVoiceAvailable: StateFlow<Boolean> = _isGenderVoiceAvailable.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        textToSpeech = TextToSpeech(appContext) { status ->
            if (status != TextToSpeech.SUCCESS) {
                _isReady.value = false
                return@TextToSpeech
            }
            val engine = textToSpeech ?: return@TextToSpeech
            val languageResult = engine.setLanguage(Locale.FRENCH)
            if (languageResult == TextToSpeech.LANG_MISSING_DATA ||
                languageResult == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                _isReady.value = false
                return@TextToSpeech
            }
            engine.setSpeechRate(DEFAULT_SPEECH_RATE)
            engine.setOnUtteranceProgressListener(
                object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        _isSpeaking.value = false
                    }
                },
            )
            isInitialized.set(true)
            scheduleApplyVoiceGender(_voiceGender.value, attempt = 0)
            _isReady.value = true
        }
    }

    fun setVoiceGender(gender: VoiceGender) {
        _voiceGender.value = gender
        speechPreferencesRepository.saveVoiceGender(gender)
        if (isInitialized.get()) {
            scheduleApplyVoiceGender(gender, attempt = 0)
        }
    }

    fun speak(text: String) {
        val engine = textToSpeech ?: return
        if (!_isReady.value || text.isBlank()) {
            return
        }
        applyVoiceGender(_voiceGender.value)
        engine.stop()
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID)
        }
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, params, UTTERANCE_ID)
    }

    fun stop() {
        textToSpeech?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        mainHandler.removeCallbacksAndMessages(null)
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized.set(false)
        _isReady.value = false
        _isSpeaking.value = false
    }

    private fun scheduleApplyVoiceGender(gender: VoiceGender, attempt: Int) {
        mainHandler.removeCallbacksAndMessages(null)
        val applied = applyVoiceGender(gender)
        val frenchVoices = getFrenchVoices()
        if (!applied && frenchVoices.isEmpty() && attempt < VOICE_APPLY_MAX_ATTEMPTS) {
            mainHandler.postDelayed(
                { scheduleApplyVoiceGender(gender, attempt + 1) },
                VOICE_APPLY_RETRY_DELAY_MS,
            )
        }
    }

    private fun applyVoiceGender(gender: VoiceGender): Boolean {
        val engine = textToSpeech ?: return false
        val frenchVoices = getFrenchVoices()
        val selectedVoice = FrenchVoiceSelector.selectVoice(
            voices = frenchVoices,
            gender = gender,
        )

        _isGenderVoiceAvailable.value = when (gender) {
            VoiceGender.MALE -> FrenchVoiceSelector.selectOnlineMaleVoice(frenchVoices) != null
            VoiceGender.FEMALE -> FrenchVoiceSelector.selectOnlineFemaleVoice(frenchVoices) != null
        }

        if (selectedVoice == null ||
            (gender == VoiceGender.FEMALE &&
                FrenchVoiceSelector.classifyVoiceGender(selectedVoice) == VoiceGender.MALE)
        ) {
            _activeVoiceLabel.value = engine.voice?.let(::formatActiveVoiceDescription)
            return false
        }

        engine.voice = selectedVoice
        _activeVoiceLabel.value = formatActiveVoiceDescription(selectedVoice)
        return true
    }

    private fun getFrenchVoices(): List<Voice> {
        val engine = textToSpeech ?: return emptyList()
        return engine.voices
            ?.filter { voice -> voice.locale.language.equals("fr", ignoreCase = true) }
            .orEmpty()
    }

    companion object {
        private const val UTTERANCE_ID = "french_speech_utterance"
        private const val VOICE_APPLY_MAX_ATTEMPTS = 8
        private const val VOICE_APPLY_RETRY_DELAY_MS = 250L
        private const val DEFAULT_SPEECH_RATE = 0.95f
    }
}
