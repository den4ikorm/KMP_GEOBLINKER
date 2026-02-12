package org.example.geoblinker.presentation.features.device


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.koin.compose.koinInject


/**
 * DeviceTwoScreen - Экран выбора модели устройства (Шаг 2 привязки)
 * 
 * Функционал:
 * - Отображение списка доступных моделей трекеров
 * - Выбор модели через RadioButton
 * - Группировка по типам (GPS, OBD, Watch)
 * - Навигация к следующему шагу
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceTwoScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStep3: (String) -> Unit,
    viewModel: DeviceViewModel = koinInject()
) {
    // Список доступных моделей устройств
    val deviceModels = remember {
        listOf(
            DeviceModel("tracker_model1", "GPS Tracker Pro", "GPS трекер с высокой точностью", "GPS"),
            DeviceModel("tracker_model2", "GPS Tracker Basic", "Базовый GPS трекер", "GPS"),
            DeviceModel("tracker_model3", "OBD Tracker", "Трекер с OBD-II интерфейсом", "OBD"),
            DeviceModel("tracker_model4", "OBD Tracker Advanced", "Расширенная диагностика авто", "OBD"),
            DeviceModel("tracker_model5", "Smart Watch Tracker", "Умные часы с GPS", "Watch"),
            DeviceModel("tracker_model6", "Kids Watch Tracker", "Детские часы с GPS", "Watch"),
        )
    }


    var selectedModel by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf("GPS") }


    val categories = listOf("GPS", "OBD", "Watch")
    val filteredModels = deviceModels.filter { it.category == selectedCategory }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Выбор модели устройства") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
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
                text = "Шаг 2 из 3",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            
            Text(
                text = "Выберите модель вашего устройства",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))


            // Категории (Chips)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            // Список моделей
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredModels) { model ->
                    DeviceModelCard(
                        model = model,
                        isSelected = selectedModel == model.id,
                        onSelect = { selectedModel = model.id }
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))


            // Кнопка "Далее"
            Button(
                onClick = {
                    selectedModel?.let { onNavigateToStep3(it) }
                },
                enabled = selectedModel != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Далее",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Composable
private fun DeviceModelCard(
    model: DeviceModel,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect
            )
            Spacer(modifier = Modifier.width(12.dp))
            
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = model.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = model.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


// Data class для модели устройства
data class DeviceModel(
    val id: String,
    val name: String,
    val description: String,
    val category: String
)
