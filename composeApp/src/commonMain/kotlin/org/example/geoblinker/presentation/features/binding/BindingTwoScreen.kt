package org.example.geoblinker.presentation.features.binding


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.koin.compose.koinInject


/**
 * BindingTwoScreen - Выбор модели и ввод IMEI
 * 
 * Функционал:
 * - Выбор модели трекера из списка
 * - Ввод IMEI номера (15 цифр)
 * - Валидация IMEI
 * - Навигация к следующему шагу
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BindingTwoScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStep3: (String, String) -> Unit,
    viewModel: DeviceViewModel = koinInject()
) {
    var selectedModel by remember { mutableStateOf("") }
    var imei by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    
    
    val deviceModels = listOf(
        "GPS Tracker Pro",
        "GPS Tracker Basic",
        "OBD Tracker",
        "OBD Tracker Advanced",
        "Smart Watch Tracker",
        "Kids Watch Tracker"
    )
    
    
    // Валидация IMEI (15 цифр)
    val isImeiValid = imei.length == 15 && imei.all { it.isDigit() }
    val canProceed = selectedModel.isNotEmpty() && isImeiValid
    
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Привязка устройства") },
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
                text = "Шаг 2 из 3",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            
            Spacer(modifier = Modifier.height(8.dp))
            
            
            Text(
                text = "Модель устройства",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            
            // Выбор модели
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedModel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Модель трекера") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    deviceModels.forEach { model ->
                        DropdownMenuItem(
                            text = { Text(model) },
                            onClick = {
                                selectedModel = model
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            
            Spacer(modifier = Modifier.height(24.dp))
            
            
            // Ввод IMEI
            OutlinedTextField(
                value = imei,
                onValueChange = {
                    if (it.length <= 15 && it.all { char -> char.isDigit() }) {
                        imei = it
                        isError = false
                    }
                },
                label = { Text("IMEI номер") },
                placeholder = { Text("123456789012345") },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text("IMEI должен содержать 15 цифр")
                    } else {
                        Text("${imei.length}/15 символов")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            
            Spacer(modifier = Modifier.height(16.dp))
            
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Где найти IMEI?",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• На коробке устройства\n• В настройках трекера\n• На наклейке под батареей\n• Наберите *#06# на телефоне",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            
            Spacer(modifier = Modifier.weight(1f))
            
            
            // Кнопка продолжения
            Button(
                onClick = {
                    if (canProceed) {
                        onNavigateToStep3(selectedModel, imei)
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = canProceed
            ) {
                Text("Продолжить")
            }
        }
    }
}
