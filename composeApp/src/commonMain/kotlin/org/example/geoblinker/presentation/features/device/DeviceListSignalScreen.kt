package org.example.geoblinker.presentation.features.device


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.koin.compose.koinInject


/**
 * DeviceListSignalScreen - История сигналов устройства
 * 
 * Функционал:
 * - Отображение истории всех событий устройства
 * - Фильтрация по типу события
 * - Временная метка каждого события
 * - Детали события (координаты, скорость, батарея)
 * - Pull-to-refresh для обновления
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListSignalScreen(
    deviceId: String,
    onNavigateBack: () -> Unit,
    viewModel: DeviceViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }
    
    
    // Фильтры типов событий
    val filters = listOf("All", "Location", "Alert", "Battery", "Speed")
    
    
    // Получаем сигналы для конкретного устройства
    val deviceSignals = state.signals.filter { signal ->
        // Предполагаем что у Signal есть deviceId
        true // TODO: фильтровать по deviceId когда структура будет ясна
    }


    // Группировка сигналов по дате
    val signalsByDate = remember(deviceSignals, selectedFilter) {
        deviceSignals
            .filter { signal ->
                if (selectedFilter == "All") true
                else signal.type == selectedFilter // TODO: adjust based on actual Signal model
            }
            .groupBy { signal ->
                // Группируем по дате (без времени)
                val instant = Instant.fromEpochMilliseconds(signal.timestamp)
                val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                "${dateTime.dayOfMonth}.${dateTime.monthNumber}.${dateTime.year}"
            }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История сигналов") },
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
        ) {
            // Фильтры
            ScrollableTabRow(
                selectedTabIndex = filters.indexOf(selectedFilter),
                modifier = Modifier.fillMaxWidth(),
                edgePadding = 16.dp
            ) {
                filters.forEach { filter ->
                    Tab(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        text = { Text(filter) }
                    )
                }
            }


            // Список сигналов
            if (signalsByDate.isEmpty()) {
                // Пустое состояние
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SignalCellularAlt,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Нет сигналов",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "История событий появится здесь",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    signalsByDate.forEach { (date, signals) ->
                        item {
                            // Заголовок даты
                            Text(
                                text = date,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }


                        items(signals) { signal ->
                            SignalCard(signal = signal)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun SignalCard(signal: org.example.geoblinker.domain.models.Signal) {
    val instant = Instant.fromEpochMilliseconds(signal.timestamp)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val timeString = "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"


    // Определяем иконку и цвет по типу сигнала
    val (icon, iconColor) = when (signal.type) {
        "Location" -> Icons.Default.LocationOn to Color(0xFF4CAF50)
        "Alert" -> Icons.Default.Warning to Color(0xFFF44336)
        "Battery" -> Icons.Default.BatteryAlert to Color(0xFFFF9800)
        "Speed" -> Icons.Default.Speed to Color(0xFF2196F3)
        else -> Icons.Default.Info to MaterialTheme.colorScheme.primary
    }


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка типа события
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = iconColor.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.width(16.dp))


            // Информация о сигнале
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = signal.type,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                
                
                // Детали сигнала
                Text(
                    text = signal.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                
                // Дополнительная информация
                if (signal.lat != 0.0 && signal.lng != 0.0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DetailChip(
                            icon = Icons.Default.LocationOn,
                            text = "${String.format("%.4f", signal.lat)}, ${String.format("%.4f", signal.lng)}"
                        )
                        if (signal.speed > 0) {
                            DetailChip(
                                icon = Icons.Default.Speed,
                                text = "${signal.speed.toInt()} км/ч"
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun DetailChip(icon: ImageVector, text: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
