package org.example.geoblinker.presentation.features.settings


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.geoblinker.presentation.viewmodels.ProfileViewModel
import org.koin.compose.koinInject


/**
 * NameSettingsScreen - Изменение имени пользователя
 * 
 * Функционал:
 * - Ввод нового имени
 * - Счетчик символов (максимум 50)
 * - Сохранение через ProfileViewModel
 * - Автоматическое закрытие при успехе
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    
    var name by remember { 
        mutableStateOf(state.profile?.name ?: "") 
    }
    
    
    // Обработка эффектов
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is org.example.geoblinker.presentation.viewmodels.ProfileEffect.ProfileUpdated -> {
                    onNavigateBack()
                }
                is org.example.geoblinker.presentation.viewmodels.ProfileEffect.ShowError -> {
                    // Error handling через Snackbar (в реальном приложении)
                }
                else -> {}
            }
        }
    }
    
    
    val maxLength = 50
    val isValid = name.isNotBlank() && name.length <= maxLength
    
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Имя пользователя") },
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
            Text(
                text = "Как к вам обращаться?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            
            OutlinedTextField(
                value = name,
                onValueChange = {
                    if (it.length <= maxLength) {
                        name = it
                    }
                },
                label = { Text("Имя") },
                placeholder = { Text("Введите ваше имя") },
                supportingText = {
                    Text("${name.length}/$maxLength символов")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            
            Spacer(modifier = Modifier.height(16.dp))
            
            
            Text(
                text = "Это имя будет отображаться в вашем профиле",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            
            Spacer(modifier = Modifier.weight(1f))
            
            
            // Кнопка сохранения
            Button(
                onClick = {
                    if (isValid) {
                        viewModel.onEvent(
                            org.example.geoblinker.presentation.viewmodels.ProfileEvent.UpdateName(name)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && isValid
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
