package org.example.geoblinker.presentation.features.settings


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.example.geoblinker.presentation.viewmodels.ProfileViewModel
import org.koin.compose.koinInject


/**
 * ConfirmationCodeSettingsScreen - Подтверждение смены телефона
 * 
 * Функционал:
 * - Ввод 6-значного кода подтверждения
 * - Таймер обратного отсчета (60 секунд)
 * - Повторная отправка кода
 * - Валидация кода
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationCodeSettingsScreen(
    phoneNumber: String,
    onNavigateBack: () -> Unit,
    onCodeConfirmed: () -> Unit,
    viewModel: ProfileViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    
    var code by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    
    
    // Таймер обратного отсчета
    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        canResend = true
    }
    
    
    val isCodeValid = code.length == 6 && code.all { it.isDigit() }
    
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Подтверждение") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
            Spacer(modifier = Modifier.height(32.dp))
            
            
            Text(
                text = "Введите код подтверждения",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            
            
            Spacer(modifier = Modifier.height(8.dp))
            
            
            Text(
                text = "Код отправлен на номер",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            
            
            Spacer(modifier = Modifier.height(32.dp))
            
            
            // Поле ввода кода
            OutlinedTextField(
                value = code,
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        code = it
                        isError = false
                    }
                },
                label = { Text("Код подтверждения") },
                placeholder = { Text("000000") },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text("Неверный код")
                    } else {
                        Text("${code.length}/6 символов")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            
            Spacer(modifier = Modifier.height(24.dp))
            
            
            // Таймер и повторная отправка
            if (!canResend) {
                Text(
                    text = "Отправить код повторно через $timeLeft сек",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                TextButton(
                    onClick = {
                        // TODO: Повторно отправить код через API
                        timeLeft = 60
                        canResend = false
                    }
                ) {
                    Text("Отправить код повторно")
                }
            }
            
            
            Spacer(modifier = Modifier.weight(1f))
            
            
            // Кнопка подтверждения
            Button(
                onClick = {
                    if (isCodeValid) {
                        // TODO: Проверить код через API
                        // Если успешно:
                        onCodeConfirmed()
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && code.length == 6
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Подтвердить")
                }
            }
        }
    }
}
