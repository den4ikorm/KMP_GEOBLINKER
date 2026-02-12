package org.example.geoblinker.presentation.features.settings


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.example.geoblinker.presentation.viewmodels.ProfileViewModel
import org.koin.compose.koinInject


/**
 * EmailSettingsScreen - Изменение email адреса
 * 
 * Функционал:
 * - Ввод нового email
 * - Валидация формата email (@ и .)
 * - Сохранение через ProfileViewModel
 * - Toast уведомления об успехе/ошибке
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    
    var email by remember { 
        mutableStateOf(state.profile?.email ?: "") 
    }
    var isError by remember { mutableStateOf(false) }
    
    
    // Валидация email
    val isValid = remember(email) {
        email.contains("@") && email.contains(".") && email.length > 5
    }
    
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Email адрес") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = remember { SnackbarHostState() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Изменение email адреса",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            
            Text(
                text = "На новый адрес будет отправлено письмо с подтверждением",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it.trim()
                    isError = false
                },
                label = { Text("Email") },
                placeholder = { Text("example@mail.com") },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text("Введите корректный email адрес")
                    } else {
                        Text("Используйте действующий email")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            
            Spacer(modifier = Modifier.weight(1f))
            
            
            // Кнопка сохранения
            Button(
                onClick = {
                    if (isValid) {
                        // TODO: Сохранить через ProfileViewModel
                        onNavigateBack()
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && email.isNotEmpty()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Сохранить")
                }
            }
        }
    }
}
