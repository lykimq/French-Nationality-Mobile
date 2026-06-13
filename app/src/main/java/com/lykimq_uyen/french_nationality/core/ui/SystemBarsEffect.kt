package com.lykimq_uyen.french_nationality.core.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.lykimq_uyen.french_nationality.ui.theme.isDarkTheme

@Composable
fun SystemBarsEffect(
    darkTheme: Boolean = isDarkTheme(),
) {
    val context = LocalContext.current

    SideEffect {
        val activity = context.findActivity() as? ComponentActivity ?: return@SideEffect
        activity.enableEdgeToEdge(
            statusBarStyle = statusBarStyle(darkTheme),
            navigationBarStyle = navigationBarStyle(darkTheme),
        )
    }
}

private fun statusBarStyle(darkTheme: Boolean): SystemBarStyle {
    return if (darkTheme) {
        SystemBarStyle.dark(Color.TRANSPARENT)
    } else {
        SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
    }
}

private fun navigationBarStyle(darkTheme: Boolean): SystemBarStyle {
    return if (darkTheme) {
        SystemBarStyle.dark(Color.TRANSPARENT)
    } else {
        SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
    }
}

private fun Context.findActivity(): Activity? {
    var current = this
    while (current is ContextWrapper) {
        if (current is Activity) {
            return current
        }
        current = current.baseContext
    }
    return null
}
