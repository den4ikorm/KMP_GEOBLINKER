package org.example.geoblinker.presentation.features.subscription


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject


/**
 * Экран подтверждения успешной оплаты (Subscription Ready)
 * Показывает успешное оформление подписки
 */
@Composable
fun SubscriptionReadyScreen(
    planName: String,
    planPrice: String,
    nextBillingDate: String,
    onNavigateToHome: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Иконка успеха
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Успех",
                modifier = Modifier.size(120.dp),
                tint = Color(0xFF4CAF50)
            )


            Spacer(modifier = Modifier.height(24.dp))


            // Заголовок
            Text(
                text = "Подписка активирована!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(16.dp))


            // Описание
            Text(
                text = "Спасибо за покупку! Ваша подписка успешно оформлена.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


            Spacer(modifier = Modifier.height(32.dp))


            // Детали подписки
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SubscriptionDetailRow(
                        label = "План:",
                        value = planName
                    )
                    
                    
                    Divider()
                    
                    
                    SubscriptionDetailRow(
                        label = "Стоимость:",
                        value = planPrice
                    )
                    
                    
                    Divider()
                    
                    
                    SubscriptionDetailRow(
                        label = "Следующее списание:",
                        value = nextBillingDate
                    )
                }
            }


            Spacer(modifier = Modifier.height(24.dp))


            // Информационная карточка
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Что дальше?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    
                    Text(
                        text = "• Все функции Premium доступны",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    
                    Text(
                        text = "• Подключите неограниченное количество устройств",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    
                    Text(
                        text = "• Настройте уведомления в профиле",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    
                    Text(
                        text = "• Отменить подписку можно в любой момент",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }


            Spacer(modifier = Modifier.weight(1f))


            // Кнопка перехода на главный экран
            Button(
                onClick = onNavigateToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Перейти на главную")
            }


            Spacer(modifier = Modifier.height(8.dp))


            // Дополнительная кнопка
            OutlinedButton(
                onClick = { /* Navigate to subscription management */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Управление подпиской")
            }
        }
    }
}


@Composable
private fun SubscriptionDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
