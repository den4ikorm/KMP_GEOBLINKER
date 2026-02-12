package org.example.geoblinker.presentation.components


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CustomSpacer(h: Int = 0, w: Int = 0) {
    Spacer(modifier = Modifier.height(h.dp).width(w.dp))
}
