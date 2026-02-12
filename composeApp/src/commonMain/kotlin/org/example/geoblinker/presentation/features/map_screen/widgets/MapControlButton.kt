package org.example.geoblinker.presentation.features.map_screen.widgets


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.example.geoblinker.presentation.features.common.utils.sdp


@Composable
fun MapControlButton(
    color: Color = Color.Transparent,
    brush: Brush? = null,
    shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val modifier = Modifier.size(40.sdp).run {
        if (brush != null) background(brush, shape) else background(color, shape)
    }.clickable(onClick = onClick)


    Box(modifier, contentAlignment = Alignment.Center, content = content)
}
