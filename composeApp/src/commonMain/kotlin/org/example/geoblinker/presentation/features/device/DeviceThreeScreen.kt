package org.example.geoblinker.presentation.features.device


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.example.geoblinker.presentation.viewmodels.states.DefaultStates
import org.koin.compose.koinInject


/**
 * DeviceThreeScreen - Экран ввода IMEI устройства (Шаг 3 привязки)
 * 
 * Функционал:
 * - Ввод IMEI (15 цифр)
 * - Валидация формата IMEI
 * - Проверка на дубликаты
 * - Привязка устройства к аккаунту
 * - Навигация к экрану ввода имени
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceThreeScreen(
    deviceModelId: String,
    onNavigateBack: () -> Unit,
    onNavigateToNameInput: () -> Unit,
    viewModel: DeviceViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    
    
    var imei by remember { mutableStateOf("") }
    var showHelp by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    // Обработка состояния ViewModel
    LaunchedEffect(state.uiState) {
        when (state.uiState) {
            is DefaultStates.Success -> {
                isLoading = false
                errorMessage = null
                onNavigateToNameInput()
            }
            is DefaultStates.Error -> {
                isLoading = false
                errorMessage = (state.uiState as DefaultStates.Error).message
            }
            else -> {}
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ввод IMEI") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { showHelp = true }) {
                        Icon(Icons.Default.Info, "Помощь")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Заголовок
            Text(
                text = "Шаг 3 из 3",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            
            Text(
                text = "Введите IMEI устройства",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            
            Text(
                text = "Модель: $deviceModelId",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))


            // Информационная карточка
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "IMEI состоит из 15 цифр и находится на корпусе устройства или в упаковке",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))


            // Поле ввода IMEI
            OutlinedTextField(
                value = imei,
                onValueChange = { 
                    if (it.length <= 15 && it.all { char -> char.isDigit() }) {
                        imei = it
                        errorMessage = null
                    }
                },
                label = { Text("IMEI устройства") },
                placeholder = { Text("123456789012345") },
                supportingText = { 
                    Text("${imei.length}/15 цифр") 
                },
                isError = errorMessage != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )


            // Сообщение об ошибке
            errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }


            Spacer(modifier = Modifier.weight(1f))


            // Кнопка "Привязать устройство"
            Button(
                onClick = {
                    if (imei.length == 15) {
                        isLoading = true
                        scope.launch {
                            viewModel.checkImei(imei)
                        }
                    } else {
                        errorMessage = "IMEI должен содержать 15 цифр"
                    }
                },
                enabled = imei.length == 15 && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Привязать устройство",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }


    // Диалог помощи
    if (showHelp) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            icon = { Icon(Icons.Default.Info, null) },
            title = { Text("Как найти IMEI?") },
            text = {
                Column {
                    Text("• Посмотрите на корпус устройства")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Проверьте упаковку или документы")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Наберите *#06# на устройстве (для некоторых моделей)")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• IMEI должен содержать ровно 15 цифр")
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelp = false }) {
                    Text("Понятно")
                }
            }
        )
    }
}
