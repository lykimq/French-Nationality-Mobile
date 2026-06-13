package com.lykimq_uyen.french_nationality.core.speech

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.lykimq_uyen.french_nationality.core.di.AppContainer

@Composable
fun rememberFrenchSpeechController(): FrenchSpeechController {
    val context = LocalContext.current
    return remember {
        AppContainer.frenchSpeechController(context)
    }
}
