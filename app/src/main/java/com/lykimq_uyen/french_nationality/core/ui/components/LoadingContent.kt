package com.lykimq_uyen.french_nationality.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lykimq_uyen.french_nationality.ui.theme.ElectricIndigo
import com.lykimq_uyen.french_nationality.ui.theme.SkyBlue

@Composable
fun LoadingContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(44.dp),
                strokeWidth = 3.dp,
                color = ElectricIndigo,
                trackColor = SkyBlue.copy(alpha = 0.25f),
            )
            Text(
                text = message,
                modifier = Modifier.padding(top = 20.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
