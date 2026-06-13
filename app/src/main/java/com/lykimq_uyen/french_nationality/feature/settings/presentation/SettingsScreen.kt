package com.lykimq_uyen.french_nationality.feature.settings.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.lykimq_uyen.french_nationality.core.speech.rememberFrenchSpeechController
import com.lykimq_uyen.french_nationality.feature.settings.presentation.components.SettingsContent

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val speechController = rememberFrenchSpeechController()
    val isSpeechReady by speechController.isReady.collectAsState()
    val voiceGender by speechController.voiceGender.collectAsState()
    val activeVoiceName by speechController.activeVoiceName.collectAsState()
    val isGenderVoiceAvailable by speechController.isGenderVoiceAvailable.collectAsState()
    val frenchVoiceOptions by speechController.frenchVoiceOptions.collectAsState()

    SettingsContent(
        voiceGender = voiceGender,
        isSpeechReady = isSpeechReady,
        isGenderVoiceAvailable = isGenderVoiceAvailable,
        activeVoiceName = activeVoiceName,
        frenchVoiceOptions = frenchVoiceOptions,
        onVoiceGenderChange = speechController::setVoiceGender,
        onVoiceSelect = speechController::selectVoiceByName,
        onTestVoiceClick = { speechController.speak(VOICE_PREVIEW_TEXT) },
        onBackClick = onBackClick,
        modifier = modifier.fillMaxSize(),
    )
}

private const val VOICE_PREVIEW_TEXT =
    "Bonjour. Voici la voix sélectionnée pour lire les questions."
