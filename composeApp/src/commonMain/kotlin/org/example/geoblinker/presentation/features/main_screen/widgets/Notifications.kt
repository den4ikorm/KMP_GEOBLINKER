package org.example.geoblinker.presentation.features.main_screen.widgets


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import geoblinker.composeapp.generated.resources.Res
import geoblinker.composeapp.generated.resources.notifications
import org.jetbrains.compose.resources.vectorResource


@Composable
fun Notifications(
    notificationsCount: Int = 0,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            Modifier.size(50.sdp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.notifications),
                contentDescription = null,
                modifier = Modifier.size(24.sdp),
                tint = Color.Unspecified
            )
        }


        if (notificationsCount > 0) {
            Box(
                Modifier
                    .size(50.sdp)
                    .offset(12.sdp, (-12).sdp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .background(
                            color = Color(0xFFF1137E),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (notificationsCount > 99) "99+" else notificationsCount.toString(),
                        modifier = Modifier.padding(horizontal = 6.sdp, vertical = 1.sdp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
