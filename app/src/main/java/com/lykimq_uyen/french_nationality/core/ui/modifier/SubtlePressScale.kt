package com.lykimq_uyen.french_nationality.core.ui.modifier

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

private const val PRESS_SCALE = 0.98f
private const val PRESS_ANIMATION_MS = 80

@Composable
fun Modifier.subtlePressScale(
    onClick: () -> Unit = {},
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) PRESS_SCALE else 1f,
        animationSpec = tween(durationMillis = PRESS_ANIMATION_MS),
        label = "subtlePressScale",
    )

    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        )
}
