package com.lykimq_uyen.french_nationality.feature.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.core.speech.FrenchVoiceOption
import com.lykimq_uyen.french_nationality.core.speech.VoiceGender
import com.lykimq_uyen.french_nationality.core.ui.components.AppGradientBackground
import com.lykimq_uyen.french_nationality.ui.theme.isDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    voiceGender: VoiceGender,
    isSpeechReady: Boolean,
    isGenderVoiceAvailable: Boolean,
    activeVoiceName: String?,
    frenchVoiceOptions: List<FrenchVoiceOption>,
    onVoiceGenderChange: (VoiceGender) -> Unit,
    onVoiceSelect: (String) -> Unit,
    onTestVoiceClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppGradientBackground(modifier = modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Retour",
                            )
                        }
                    },
                    title = { Text("Paramètres") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                VoiceGenderSettingCard(
                    voiceGender = voiceGender,
                    isSpeechReady = isSpeechReady,
                    isGenderVoiceAvailable = isGenderVoiceAvailable,
                    activeVoiceName = activeVoiceName,
                    frenchVoiceOptions = frenchVoiceOptions,
                    onVoiceGenderChange = onVoiceGenderChange,
                    onVoiceSelect = onVoiceSelect,
                    onTestVoiceClick = onTestVoiceClick,
                )
            }
        }
    }
}

@Composable
private fun VoiceGenderSettingCard(
    voiceGender: VoiceGender,
    isSpeechReady: Boolean,
    isGenderVoiceAvailable: Boolean,
    activeVoiceName: String?,
    frenchVoiceOptions: List<FrenchVoiceOption>,
    onVoiceGenderChange: (VoiceGender) -> Unit,
    onVoiceSelect: (String) -> Unit,
    onTestVoiceClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val genderOptions = frenchVoiceOptions.filter { option ->
        option.gender == voiceGender
    }
    val selectableOptions = if (genderOptions.isNotEmpty()) {
        genderOptions
    } else {
        frenchVoiceOptions
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = settingsCardColor()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Voix de lecture",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Choisis la voix utilisée pour lire les questions et les explications.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = voiceGender == VoiceGender.MALE,
                    onClick = { onVoiceGenderChange(VoiceGender.MALE) },
                    enabled = isSpeechReady,
                    label = { Text("Voix homme") },
                    modifier = Modifier.weight(1f),
                )
                FilterChip(
                    selected = voiceGender == VoiceGender.FEMALE,
                    onClick = { onVoiceGenderChange(VoiceGender.FEMALE) },
                    enabled = isSpeechReady,
                    label = { Text("Voix femme") },
                    modifier = Modifier.weight(1f),
                )
            }

            if (isSpeechReady && selectableOptions.isNotEmpty()) {
                Text(
                    text = if (genderOptions.isNotEmpty()) {
                        "Voix disponibles"
                    } else {
                        "Choisis manuellement une voix"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                selectableOptions.forEach { option ->
                    VoiceOptionRow(
                        option = option,
                        selected = option.id == activeVoiceName,
                        onSelect = { onVoiceSelect(option.id) },
                    )
                }
            }

            TextButton(
                onClick = onTestVoiceClick,
                enabled = isSpeechReady,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text("Tester la voix")
            }

            if (!isSpeechReady) {
                Text(
                    text = "Voix française indisponible sur cet appareil.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else if (!isGenderVoiceAvailable && voiceGender == VoiceGender.FEMALE) {
                Text(
                    text = "Aucune voix féminine détectée. Installe une voix française " +
                        "dans Paramètres Android > Langue et saisie > Synthèse vocale, " +
                        "puis choisis une voix ci-dessus.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun VoiceOptionRow(
    option: FrenchVoiceOption,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
        )
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Text(
                text = option.label,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (option.gender != null) {
                Text(
                    text = when (option.gender) {
                        VoiceGender.MALE -> "Homme"
                        VoiceGender.FEMALE -> "Femme"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun settingsCardColor(): Color {
    return if (isDarkTheme()) {
        Color(0xFF252036).copy(alpha = 0.92f)
    } else {
        Color.White.copy(alpha = 0.94f)
    }
}
