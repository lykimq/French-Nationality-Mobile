package com.lykimq_uyen.french_nationality

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lykimq_uyen.french_nationality.core.navigation.AppNavHost
import com.lykimq_uyen.french_nationality.ui.theme.FrenchNationalityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = initialStatusBarStyle(),
            navigationBarStyle = initialNavigationBarStyle(),
        )
        setContent {
            FrenchNationalityTheme {
                AppNavHost()
            }
        }
    }

    private fun isDarkTheme(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES
    }

    private fun initialStatusBarStyle(): SystemBarStyle {
        return if (isDarkTheme()) {
            SystemBarStyle.dark(Color.TRANSPARENT)
        } else {
            SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        }
    }

    private fun initialNavigationBarStyle(): SystemBarStyle {
        return if (isDarkTheme()) {
            SystemBarStyle.dark(Color.TRANSPARENT)
        } else {
            SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        }
    }
}
