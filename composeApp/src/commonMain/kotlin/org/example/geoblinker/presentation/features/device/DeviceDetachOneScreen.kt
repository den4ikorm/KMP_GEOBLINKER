package org.example.geoblinker.presentation.features.device


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.koin.compose.koinInject


/**
 * DeviceDetachOneScreen - Предупреждение перед отвязкой устройства (Шаг 1)
 * 
 * Функционал:
 * - Отображение информации об устройстве
 * - Предупреждение о последствиях отвязки
 * - Информация о потере данных
 * - Кнопки "Отмена" и "Продолжить"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetachOneScreen(
    deviceId: String,
    onNavigateBack: () -> Unit,
    onNavigateToStep2: (String) -> Unit,
    viewModel: DeviceViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    
    // Находим устройство по ID
    val device = state.devices.firstOrNull { it.id == deviceId }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Отвязка устройства") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))


            // Иконка предупреждения
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.size(96.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }


            Spacer(modifier = Modifier.height(24.dp))


            // Заголовок
            Text(
                text = "Отвязка устройства",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(16.dp))


            // Информация об устройстве
            device?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        InfoRow(label = "Устройство:", value = it.name)
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoRow(label = "IMEI:", value = it.imei)
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoRow(label = "Модель:", value = it.deviceType)
                    }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))


            // Предупреждения
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "⚠️ Важно:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    
                    WarningItem("Вы потеряете доступ к отслеживанию устройства")
                    Spacer(modifier = Modifier.height(8.dp))
                    WarningItem("Вся история перемещений будет удалена")
                    Spacer(modifier = Modifier.height(8.dp))
                    WarningItem("Настройки и геозоны будут сброшены")
                    Spacer(modifier = Modifier.height(8.dp))
                    WarningItem("Это действие нельзя отменить")
                }
            }


            Spacer(modifier = Modifier.weight(1f))


            // Информация о повторной привязке
            Text(
                text = "Вы сможете привязать это устройство заново в любое время",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(16.dp))


            // Кнопки действий
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onNavigateToStep2(deviceId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = "Продолжить отвязку",
                        style = MaterialTheme.typography.titleMedium
                    )
                }


                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Отмена",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
private fun WarningItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "• ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}
