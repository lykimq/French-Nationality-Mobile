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
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.core.billing.DonationMessage
import com.lykimq_uyen.french_nationality.core.billing.DonationUiState
import com.lykimq_uyen.french_nationality.core.settings.ThemeMode
import com.lykimq_uyen.french_nationality.core.speech.VoiceGender
import com.lykimq_uyen.french_nationality.core.ui.components.AppGradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    voiceGender: VoiceGender,
    themeMode: ThemeMode,
    isSpeechReady: Boolean,
    appVersionName: String,
    showResetProgressDialog: Boolean,
    donationState: DonationUiState,
    donationMessage: DonationMessage?,
    onVoiceGenderChange: (VoiceGender) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    onTestVoiceClick: () -> Unit,
    onResetProgressClick: () -> Unit,
    onConfirmResetProgress: () -> Unit,
    onDismissResetProgressDialog: () -> Unit,
    onDonateCoffeeClick: () -> Unit,
    onDismissDonationMessage: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(donationMessage) {
        when (val message = donationMessage) {
            null -> Unit
            DonationMessage.ThankYou -> {
                snackbarHostState.showSnackbar("Merci pour ton soutien !")
                onDismissDonationMessage()
            }
            DonationMessage.Cancelled -> {
                onDismissDonationMessage()
            }
            is DonationMessage.Error -> {
                snackbarHostState.showSnackbar(message.text)
                onDismissDonationMessage()
            }
        }
    }

    AppGradientBackground(modifier = modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                SettingsSectionHeader(title = "Affichage")
                SettingsGroupCard {
                    SettingsRowLabel(label = "Thème")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ThemeMode.entries.forEach { mode ->
                            FilterChip(
                                selected = themeMode == mode,
                                onClick = { onThemeModeChange(mode) },
                                label = { Text(themeModeLabel(mode)) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                SettingsSectionHeader(title = "Lecture")
                SettingsGroupCard {
                    SettingsRowLabel(label = "Voix")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        FilterChip(
                            selected = voiceGender == VoiceGender.MALE,
                            onClick = { onVoiceGenderChange(VoiceGender.MALE) },
                            enabled = isSpeechReady,
                            label = { Text("Homme") },
                            modifier = Modifier.weight(1f),
                        )
                        FilterChip(
                            selected = voiceGender == VoiceGender.FEMALE,
                            onClick = { onVoiceGenderChange(VoiceGender.FEMALE) },
                            enabled = isSpeechReady,
                            label = { Text("Femme") },
                            modifier = Modifier.weight(1f),
                        )
                        IconButton(
                            onClick = onTestVoiceClick,
                            enabled = isSpeechReady,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Tester la voix",
                            )
                        }
                    }
                }

                SettingsSectionHeader(title = "Étude")
                SettingsGroupCard {
                    SettingsRowLabel(label = "Progression")
                    OutlinedButton(
                        onClick = onResetProgressClick,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Réinitialiser la progression")
                    }
                }

                SettingsSectionHeader(title = "Soutenir l'app")
                SettingsGroupCard {
                    Text(
                        text = "Si cette app t'aide à préparer l'entretien, tu peux m'offrir un café.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    when (donationState) {
                        DonationUiState.Loading -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        DonationUiState.Unavailable -> {
                            Text(
                                text = "Les dons via Google Play seront disponibles après publication de l'app.",
                                modifier = Modifier.padding(top = 12.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        is DonationUiState.Ready -> {
                            Button(
                                onClick = onDonateCoffeeClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Coffee,
                                        contentDescription = null,
                                    )
                                    Text(
                                        text = "Offrir un café (${donationState.product.formattedPrice})",
                                        modifier = Modifier.padding(start = 8.dp),
                                    )
                                }
                            }
                        }
                        DonationUiState.Processing -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                SettingsSectionHeader(title = "À propos")
                SettingsGroupCard {
                    Text(
                        text = "Naturalisation FR",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "Version $appVersionName",
                        modifier = Modifier.padding(top = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }

    if (showResetProgressDialog) {
        AlertDialog(
            onDismissRequest = onDismissResetProgressDialog,
            title = { Text("Réinitialiser la progression ?") },
            text = {
                Text(
                    "Toute ta progression de lecture sera effacée. " +
                        "Tu repartiras au début de chaque section.",
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirmResetProgress) {
                    Text("Effacer")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissResetProgressDialog) {
                    Text("Annuler")
                }
            },
        )
    }
}

private fun themeModeLabel(mode: ThemeMode): String {
    return when (mode) {
        ThemeMode.SYSTEM -> "Système"
        ThemeMode.LIGHT -> "Clair"
        ThemeMode.DARK -> "Sombre"
    }
}
