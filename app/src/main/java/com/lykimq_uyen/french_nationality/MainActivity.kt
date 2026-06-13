package com.lykimq_uyen.french_nationality

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lykimq_uyen.french_nationality.feature.home.presentation.HomeScreen
import com.lykimq_uyen.french_nationality.ui.theme.FrenchNationalityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        setContent {
            FrenchNationalityTheme {
                HomeScreen()
            }
        }
    }
}
