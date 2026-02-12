package org.example.geoblinker.presentation.features.device


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.koin.compose.koinInject


/**
 * DeviceDetachTwoScreen - Подтверждение отвязки устройства (Шаг 2)
 * 
 * Функционал:
 * - Ввод подтверждающего кода (слово "ОТВЯЗАТЬ")
 * - Финальное подтверждение действия
 * - Процесс отвязки с loading state
 * - Успешное завершение с автоперенаправлением
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetachTwoScreen(
    deviceId: String,
    onNavigateBack: () -> Unit,
    onNavigateToDeviceList: () -> Unit,
    viewModel: DeviceViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    
    
    var confirmationText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    val requiredText = "ОТВЯЗАТЬ"
    val isConfirmed = confirmationText.uppercase() == requiredText
    
    
    // Находим устройство
    val device = state.devices.firstOrNull { it.id == deviceId }


    // Автоперенаправление после успеха
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            delay(2000)
            onNavigateToDeviceList()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Подтверждение отвязки") },
                navigationIcon = {
                    if (!isSuccess) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            )
        }
    ) { padding ->
        if (isSuccess) {
            // Экран успеха
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(96.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Устройство отвязано",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Перенаправление...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Форма подтверждения
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "Финальное подтверждение",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))


                Text(
                    text = "Это действие необратимо. Все данные устройства будут удалены.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))


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
                            Text(
                                text = "Отвязываемое устройство:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "IMEI: ${it.imei}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(32.dp))


                // Инструкция
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Для подтверждения введите слово:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = requiredText,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))


                // Поле ввода подтверждения
                OutlinedTextField(
                    value = confirmationText,
                    onValueChange = {
                        confirmationText = it
                        errorMessage = null
                    },
                    label = { Text("Введите '$requiredText'") },
                    placeholder = { Text(requiredText) },
                    isError = errorMessage != null,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isConfirmed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )


                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }


                if (isConfirmed) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✓ Подтверждение введено верно",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                Spacer(modifier = Modifier.weight(1f))


                // Кнопки
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            if (isConfirmed) {
                                isLoading = true
                                scope.launch {
                                    // TODO: Вызов метода отвязки из ViewModel
                                    delay(1500) // Имитация запроса
                                    isLoading = false
                                    isSuccess = true
                                }
                            } else {
                                errorMessage = "Введите '$requiredText' для подтверждения"
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onError
                            )
                        } else {
                            Text(
                                text = "Отвязать устройство",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }


                    OutlinedButton(
                        onClick = onNavigateBack,
                        enabled = !isLoading,
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
}
