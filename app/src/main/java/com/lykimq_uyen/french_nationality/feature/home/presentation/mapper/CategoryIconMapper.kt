package com.lykimq_uyen.french_nationality.feature.home.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientBlueEnd
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientBlueStart
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientCoralEnd
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientCoralStart
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientCyanEnd
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientCyanStart
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientMintEnd
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientMintStart
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientSunEnd
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientSunStart
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientVioletEnd
import com.lykimq_uyen.french_nationality.ui.theme.CategoryGradientVioletStart
import com.lykimq_uyen.french_nationality.ui.theme.FreshMint
import com.lykimq_uyen.french_nationality.ui.theme.PopCoral
import com.lykimq_uyen.french_nationality.ui.theme.SkyBlue
import com.lykimq_uyen.french_nationality.ui.theme.isDarkTheme

data class CategoryVisual(
    val icon: ImageVector,
    val gradientStart: Color,
    val gradientEnd: Color,
)

@Composable
fun categoryVisual(iconKey: String): CategoryVisual {
    val palette = if (isDarkTheme()) darkPalette(iconKey) else lightPalette(iconKey)
    return CategoryVisual(
        icon = categoryIcon(iconKey),
        gradientStart = palette.start,
        gradientEnd = palette.end,
    )
}

private fun categoryIcon(iconKey: String): ImageVector {
    return when (iconKey) {
        "flag" -> Icons.Outlined.Flag
        "institution" -> Icons.Outlined.AccountBalance
        "scale" -> Icons.Outlined.Balance
        "map" -> Icons.Outlined.Map
        "home" -> Icons.Outlined.Home
        "person" -> Icons.Outlined.Person
        else -> Icons.AutoMirrored.Outlined.MenuBook
    }
}

private data class GradientPalette(
    val start: Color,
    val end: Color,
)

private fun lightPalette(iconKey: String): GradientPalette {
    return when (iconKey) {
        "flag" -> GradientPalette(CategoryGradientBlueStart, CategoryGradientBlueEnd)
        "institution" -> GradientPalette(CategoryGradientVioletStart, CategoryGradientVioletEnd)
        "scale" -> GradientPalette(CategoryGradientVioletStart, CategoryGradientCyanEnd)
        "map" -> GradientPalette(CategoryGradientMintStart, CategoryGradientMintEnd)
        "home" -> GradientPalette(CategoryGradientSunStart, CategoryGradientSunEnd)
        "person" -> GradientPalette(CategoryGradientCyanStart, CategoryGradientCoralEnd)
        else -> GradientPalette(CategoryGradientBlueStart, CategoryGradientBlueEnd)
    }
}

private fun darkPalette(iconKey: String): GradientPalette {
    return when (iconKey) {
        "flag" -> GradientPalette(CategoryGradientBlueEnd, SkyBlue)
        "institution" -> GradientPalette(CategoryGradientVioletEnd, CategoryGradientCyanStart)
        "scale" -> GradientPalette(CategoryGradientVioletStart, SkyBlue)
        "map" -> GradientPalette(FreshMint, CategoryGradientMintEnd)
        "home" -> GradientPalette(CategoryGradientSunEnd, PopCoral)
        "person" -> GradientPalette(CategoryGradientCyanStart, CategoryGradientCoralStart)
        else -> GradientPalette(CategoryGradientBlueEnd, CategoryGradientVioletStart)
    }
}
