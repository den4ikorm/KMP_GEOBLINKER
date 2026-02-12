package org.example.geoblinker.presentation.features.main_screen.widgets


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.TypeColor
import geoblinker.composeapp.generated.resources.*
import geoblinker.composeapp.generated.resources.Res
import org.example.geoblinker.presentation.features.common.utils.sdp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun TopBar(
    isMapSelected: Boolean,
    isListSelected: Boolean,
    countNotifications: Int,
    avatarUri: String?,
    onProfileClick: () -> Unit,
    onMapClick: () -> Unit,
    onListClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Surface(
        modifier = Modifier.shadow(
            elevation = 4.sdp,
            shape = RoundedCornerShape(bottomStart = 30.sdp, bottomEnd = 30.sdp),
            clip = false,
            ambientColor = Color.Black,
            spotColor = Color.Black.copy(0.25f)
        ),
        shape = RoundedCornerShape(bottomStart = 30.sdp, bottomEnd = 30.sdp),
        color = Color.White.copy(alpha = 0.6f)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.sdp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                var isImageLoaded by remember { mutableStateOf(false) }


                if (!avatarUri.isNullOrEmpty()) {
                    AsyncImage(
                        model = avatarUri,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.sdp)
                            .clickable(onClick = onProfileClick),
                        contentScale = ContentScale.Crop,
                        onState = { state ->
                            isImageLoaded = state is AsyncImagePainter.State.Success
                        }
                    )
                }


                if (avatarUri.isNullOrEmpty() || !isImageLoaded) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.user_without_photo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.sdp)
                            .clickable(onClick = onProfileClick),
                        tint = Color.Unspecified
                    )
                }
            }


            Spacer(Modifier.width(10.sdp))


            CustomButton(
                modifier = Modifier.width(105.sdp),
                text = stringResource(Res.string.map),
                onClick = onMapClick,
                typeColor = if (isMapSelected) TypeColor.Green1 else TypeColor.White,
                height = 50,
                radius = 100
            )


            Spacer(Modifier.width(10.sdp))


            CustomButton(
                modifier = Modifier.width(105.sdp),
                text = stringResource(Res.string.list),
                onClick = onListClick,
                typeColor = if (isListSelected) TypeColor.Green1 else TypeColor.White,
                height = 50,
                radius = 100
            )


            Spacer(Modifier.width(10.sdp))


            Notifications(
                notificationsCount = countNotifications,
                onClick = onNotificationsClick
            )
        }
    }
}
