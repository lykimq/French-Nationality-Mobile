package com.lykimq_uyen.french_nationality.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.lykimq_uyen.french_nationality.core.settings.ThemeMode
import com.lykimq_uyen.french_nationality.core.ui.SystemBarsEffect

private val LightColors = lightColorScheme(
    primary = ElectricIndigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = Color(0xFF312E81),
    secondary = DeepCyan,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0C4A6E),
    tertiary = FreshMint,
    onTertiary = Color(0xFF042F2E),
    background = GenZBackgroundTop,
    onBackground = GenZTextPrimary,
    surface = GenZSurfaceLight,
    onSurface = GenZTextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = GenZTextSecondary,
    outline = Color(0xFFE2E8F0),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA5B4FC),
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = Color(0xFF4338CA),
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF7DD3FC),
    onSecondary = Color(0xFF0C4A6E),
    secondaryContainer = Color(0xFF0E3A5F),
    onSecondaryContainer = Color(0xFFE0F2FE),
    tertiary = Color(0xFF5EEAD4),
    onTertiary = Color(0xFF042F2E),
    background = GenZDarkBackgroundTop,
    onBackground = GenZTextPrimaryDark,
    surface = GenZSurfaceDark,
    onSurface = GenZTextPrimaryDark,
    surfaceVariant = Color(0xFF2D2640),
    onSurfaceVariant = GenZTextSecondaryDark,
    outline = Color(0xFF475569),
)

val LocalThemeMode = staticCompositionLocalOf { ThemeMode.SYSTEM }

@Composable
fun FrenchNationalityTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme = resolveDarkTheme(themeMode)

    CompositionLocalProvider(LocalThemeMode provides themeMode) {
        SystemBarsEffect(darkTheme = darkTheme)

        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}

@Composable
fun isDarkTheme(): Boolean = resolveDarkTheme(LocalThemeMode.current)

@Composable
private fun resolveDarkTheme(themeMode: ThemeMode): Boolean {
    return when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
}
