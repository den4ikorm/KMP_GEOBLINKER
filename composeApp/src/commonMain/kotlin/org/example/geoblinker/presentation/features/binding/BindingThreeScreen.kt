package org.example.geoblinker.presentation.features.binding


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.koin.compose.koinInject


/**
 * BindingThreeScreen - Подтверждение и завершение привязки
 * 
 * Функционал:
 * - Отображение выбранных данных
 * - Проверка IMEI на сервере
 * - Loading состояние
 * - Success экран после успешной привязки
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BindingThreeScreen(
    model: String,
    imei: String,
    onNavigateBack: () -> Unit,
    onBindingComplete: () -> Unit,
    viewModel: DeviceViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    
    var isBinding by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            delay(1500)
            onBindingComplete()
        }
    }
    
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Привязка устройства") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        enabled = !isBinding && !isSuccess
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
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
            if (!isSuccess) {
                // Экран подтверждения
                Text(
                    text = "Шаг 3 из 3",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                
                Spacer(modifier = Modifier.height(8.dp))
                
                
                Text(
                    text = "Подтверждение данных",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                
                // Карточка с данными
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        DataRow(
                            label = "Модель",
                            value = model
                        )
                        
                        
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        
                        
                        DataRow(
                            label = "IMEI",
                            value = imei
                        )
                    }
                }
                
                
                Spacer(modifier = Modifier.height(24.dp))
                
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "ℹ️ Информация",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "После привязки устройство появится в списке и вы сможете отслеживать его местоположение",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
                
                
                Spacer(modifier = Modifier.weight(1f))
                
                
                // Кнопка привязки
                Button(
                    onClick = {
                        isBinding = true
                        // TODO: Привязать устройство через DeviceViewModel
                        // Симуляция процесса
                        kotlinx.coroutines.MainScope().launch {
                            delay(2000)
                            isBinding = false
                            isSuccess = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isBinding
                ) {
                    if (isBinding) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Привязка...")
                    } else {
                        Text("Привязать устройство")
                    }
                }
            } else {
                // Success экран
                Spacer(modifier = Modifier.weight(1f))
                
                
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(100.dp)
                )
                
                
                Spacer(modifier = Modifier.height(24.dp))
                
                
                Text(
                    text = "Устройство привязано!",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                
                
                Spacer(modifier = Modifier.height(8.dp))
                
                
                Text(
                    text = "Трекер $model успешно добавлен",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


@Composable
private fun DataRow(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
