package org.example.geoblinker.presentation.features.main_screen.widgets


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.geoblinker.ui.CustomButton
import com.example.geoblinker.ui.TypeColor
import geoblinker.composeapp.generated.resources.Res
import geoblinker.composeapp.generated.resources.wallet
import org.jetbrains.compose.resources.vectorResource


@Composable
fun SubscriptionExpiredDialog(onPayClick: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.width(310.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 25.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.wallet),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        "Бесплатный пробный период закончился. Оплатите подписку, чтобы пользоваться функциями приложения GeoBlinker.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(28.dp))
                    CustomButton(
                        text = "Перейти к оплате",
                        onClick = onPayClick,
                        typeColor = TypeColor.Green,
                        height = 55
                    )
                }
            }
        }
    }
}
