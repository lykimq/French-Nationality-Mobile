package com.lykimq_uyen.french_nationality.feature.settings.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lykimq_uyen.french_nationality.core.speech.rememberFrenchSpeechController
import com.lykimq_uyen.french_nationality.feature.settings.presentation.components.SettingsContent

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(),
) {
    val speechController = rememberFrenchSpeechController()
    val isSpeechReady by speechController.isReady.collectAsState()
    val voiceGender by speechController.voiceGender.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val showResetProgressDialog by viewModel.showResetProgressDialog.collectAsState()

    SettingsContent(
        voiceGender = voiceGender,
        themeMode = themeMode,
        isSpeechReady = isSpeechReady,
        appVersionName = viewModel.appVersionName,
        showResetProgressDialog = showResetProgressDialog,
        onVoiceGenderChange = speechController::setVoiceGender,
        onThemeModeChange = viewModel::setThemeMode,
        onTestVoiceClick = {
            speechController.speak(
                "Bonjour. Voici la voix sélectionnée pour lire les questions.",
            )
        },
        onResetProgressClick = viewModel::requestResetProgress,
        onConfirmResetProgress = viewModel::confirmResetProgress,
        onDismissResetProgressDialog = viewModel::dismissResetProgressDialog,
        onBackClick = onBackClick,
        modifier = modifier.fillMaxSize(),
    )
}
