package org.example.geoblinker.presentation.features.settings


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.geoblinker.presentation.viewmodels.ProfileViewModel
import org.koin.compose.koinInject


/**
 * DeleteAccountSettingsScreen - Удаление аккаунта
 * 
 * Функционал:
 * - Предупреждение о последствиях удаления
 * - Диалог с подтверждением
 * - Ввод слова "УДАЛИТЬ" для подтверждения
 * - Навигация к Auth после удаления
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountSettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAuth: () -> Unit,
    viewModel: ProfileViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    
    var showDialog by remember { mutableStateOf(false) }
    var confirmText by remember { mutableStateOf("") }
    
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Удаление аккаунта") },
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
                .padding(16.dp)
        ) {
            // Предупреждение
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column {
                        Text(
                            text = "Внимание!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Это действие необратимо",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            
            
            Text(
                text = "При удалении аккаунта будут удалены:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            
            val consequences = listOf(
                "Все ваши персональные данные",
                "История местоположений устройств",
                "Настройки геозон и уведомлений",
                "Активная подписка (без возврата средств)",
                "Привязанные устройства"
            )
            
            
            consequences.forEach { item ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("• ", color = MaterialTheme.colorScheme.error)
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            
            Spacer(modifier = Modifier.weight(1f))
            
            
            // Кнопка удаления
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onError,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Удалить аккаунт")
                }
            }
        }
    }
    
    
    // Диалог подтверждения
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { 
                showDialog = false
                confirmText = ""
            },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text("Подтверждение удаления")
            },
            text = {
                Column {
                    Text(
                        text = "Введите слово УДАЛИТЬ для подтверждения",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = confirmText,
                        onValueChange = { confirmText = it.uppercase() },
                        placeholder = { Text("УДАЛИТЬ") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (confirmText == "УДАЛИТЬ") {
                            // TODO: Удалить аккаунт через ProfileViewModel
                            showDialog = false
                            onNavigateToAuth()
                        }
                    },
                    enabled = confirmText == "УДАЛИТЬ",
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Удалить навсегда")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showDialog = false
                        confirmText = ""
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}
