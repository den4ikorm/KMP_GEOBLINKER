package org.example.geoblinker.presentation.features.settings


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.geoblinker.presentation.viewmodels.ProfileViewModel
import org.koin.compose.koinInject


/**
 * UnitDistanceSettingsScreen - Настройки единиц измерения расстояния
 * 
 * Функционал:
 * - Выбор системы измерения (Метрическая/Имперская)
 * - Сохранение в профиль пользователя
 * - Автоматическое закрытие после сохранения
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDistanceSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    
    var selectedUnit by remember { 
        mutableStateOf(state.profile?.unitSystem ?: "metric") 
    }
    
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Единицы измерения") },
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
                text = "Выберите систему измерения расстояния",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            
            // Метрическая система
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedUnit == "metric",
                        onClick = { selectedUnit = "metric" }
                    )
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedUnit == "metric",
                    onClick = { selectedUnit = "metric" }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Метрическая",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Километры (км), метры (м)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            
            Divider()
            
            
            // Имперская система
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedUnit == "imperial",
                        onClick = { selectedUnit = "imperial" }
                    )
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedUnit == "imperial",
                    onClick = { selectedUnit = "imperial" }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Имперская",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Мили (mi), футы (ft)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            
            Spacer(modifier = Modifier.weight(1f))
            
            
            // Кнопка сохранения
            Button(
                onClick = {
                    // TODO: Сохранить через ProfileViewModel
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
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
