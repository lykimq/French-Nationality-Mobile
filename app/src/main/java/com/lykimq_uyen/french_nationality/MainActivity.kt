package com.lykimq_uyen.french_nationality

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.lykimq_uyen.french_nationality.core.di.AppContainer
import com.lykimq_uyen.french_nationality.core.navigation.AppNavHost
import com.lykimq_uyen.french_nationality.ui.theme.FrenchNationalityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeController = remember { AppContainer.appThemeController(this) }
            val themeMode by themeController.themeMode.collectAsState()

            FrenchNationalityTheme(themeMode = themeMode) {
                AppNavHost()
            }
        }
    }
}
