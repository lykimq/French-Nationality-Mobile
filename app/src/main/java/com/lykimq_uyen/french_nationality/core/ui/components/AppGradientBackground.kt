package com.lykimq_uyen.french_nationality.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.lykimq_uyen.french_nationality.ui.theme.GenZBackgroundBottom
import com.lykimq_uyen.french_nationality.ui.theme.GenZBackgroundMid
import com.lykimq_uyen.french_nationality.ui.theme.GenZBackgroundTop
import com.lykimq_uyen.french_nationality.ui.theme.GenZDarkBackgroundBottom
import com.lykimq_uyen.french_nationality.ui.theme.GenZDarkBackgroundMid
import com.lykimq_uyen.french_nationality.ui.theme.GenZDarkBackgroundTop
import com.lykimq_uyen.french_nationality.ui.theme.isDarkTheme

@Composable
fun AppGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val brush = if (isDarkTheme()) darkGradient() else lightGradient()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush),
    ) {
        content()
    }
}

private fun lightGradient(): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            GenZBackgroundTop,
            GenZBackgroundMid,
            GenZBackgroundBottom,
        ),
    )
}

private fun darkGradient(): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            GenZDarkBackgroundTop,
            GenZDarkBackgroundMid,
            GenZDarkBackgroundBottom,
        ),
    )
}
