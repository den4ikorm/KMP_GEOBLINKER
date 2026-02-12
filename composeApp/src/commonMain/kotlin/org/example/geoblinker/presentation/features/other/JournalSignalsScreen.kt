package org.example.geoblinker.presentation.features.other


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.koin.compose.koinInject


/**
 * Экран журнала сигналов устройства
 * Показывает историю всех событий и сигналов
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalSignalsScreen(
    deviceId: String,
    onNavigateBack: () -> Unit,
    viewModel: DeviceViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    
    LaunchedEffect(deviceId) {
        viewModel.handleEvent(DeviceViewModel.Event.LoadSignalHistory(deviceId))
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Журнал сигналов") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                val signals = getSampleSignals() // В реальности из state
                
                
                if (signals.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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
                        Text(
                            text = "История сигналов пуста",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Группировка по датам
                        val groupedSignals = signals.groupBy { it.date }
                        
                        
                        groupedSignals.forEach { (date, signalsForDate) ->
                            item {
                                Text(
                                    text = date,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            
                            
                            items(signalsForDate) { signal ->
                                SignalCard(signal = signal)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun SignalCard(signal: SignalData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (signal.type) {
                SignalType.CRITICAL -> MaterialTheme.colorScheme.errorContainer
                SignalType.WARNING -> MaterialTheme.colorScheme.tertiaryContainer
                SignalType.INFO -> MaterialTheme.colorScheme.surfaceVariant
                SignalType.SUCCESS -> MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Иконка типа сигнала
            Surface(
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.medium,
                color = when (signal.type) {
                    SignalType.CRITICAL -> MaterialTheme.colorScheme.error
                    SignalType.WARNING -> MaterialTheme.colorScheme.tertiary
                    SignalType.INFO -> MaterialTheme.colorScheme.primary
                    SignalType.SUCCESS -> Color(0xFF4CAF50)
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = signal.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            
            // Информация о сигнале
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = signal.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    
                    Text(
                        text = signal.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                
                Text(
                    text = signal.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                
                if (signal.location.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = signal.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}


// Типы сигналов
private enum class SignalType {
    CRITICAL, WARNING, INFO, SUCCESS
}


// Модель данных сигнала
private data class SignalData(
    val id: String,
    val date: String,
    val time: String,
    val type: SignalType,
    val icon: ImageVector,
    val title: String,
    val description: String,
    val location: String = ""
)


// Примеры сигналов (в реальности из ViewModel)
private fun getSampleSignals(): List<SignalData> = listOf(
    SignalData(
        id = "1",
        date = "Сегодня",
        time = "14:35",
        type = SignalType.CRITICAL,
        icon = Icons.Default.BatteryAlert,
        title = "Низкий заряд батареи",
        description = "Осталось 5% заряда. Требуется зарядка.",
        location = "Москва, ул. Ленина, 15"
    ),
    SignalData(
        id = "2",
        date = "Сегодня",
        time = "12:20",
        type = SignalType.WARNING,
        icon = Icons.Default.Speed,
        title = "Превышение скорости",
        description = "Скорость 95 км/ч (лимит: 60 км/ч)",
        location = "Московская обл., М-7"
    ),
    SignalData(
        id = "3",
        date = "Сегодня",
        time = "09:15",
        type = SignalType.INFO,
        icon = Icons.Default.ExitToApp,
        title = "Выход из геозоны",
        description = "Устройство покинуло зону 'Дом'",
        location = "Москва, ул. Пушкина, 10"
    ),
    SignalData(
        id = "4",
        date = "Вчера",
        time = "18:45",
        type = SignalType.SUCCESS,
        icon = Icons.Default.LocationOn,
        title = "Вход в геозону",
        description = "Устройство вошло в зону 'Работа'",
        location = "Москва, Красная площадь, 1"
    ),
    SignalData(
        id = "5",
        date = "Вчера",
        time = "15:30",
        type = SignalType.INFO,
        icon = Icons.Default.PowerOff,
        title = "Устройство отключено",
        description = "Потеря связи с устройством",
        location = "Последняя позиция: Москва"
    ),
    SignalData(
        id = "6",
        date = "Вчера",
        time = "08:00",
        type = SignalType.SUCCESS,
        icon = Icons.Default.Power,
        title = "Устройство включено",
        description = "Связь с устройством восстановлена",
        location = "Москва, ул. Пушкина, 10"
    ),
    SignalData(
        id = "7",
        date = "02.02.2026",
        time = "20:15",
        type = SignalType.WARNING,
        icon = Icons.Default.Timer,
        title = "Длительная стоянка",
        description = "Устройство неподвижно более 2 часов",
        location = "Москва, ул. Ленина, 15"
    ),
    SignalData(
        id = "8",
        date = "02.02.2026",
        time = "14:00",
        type = SignalType.INFO,
        icon = Icons.Default.Sync,
        title = "Обновление данных",
        description = "Успешная синхронизация с сервером",
        location = ""
    ),
    SignalData(
        id = "9",
        date = "01.02.2026",
        time = "22:30",
        type = SignalType.CRITICAL,
        icon = Icons.Default.Warning,
        title = "Удар/падение",
        description = "Обнаружен сильный удар",
        location = "Москва, ул. Ленина, 15"
    ),
    SignalData(
        id = "10",
        date = "01.02.2026",
        time = "10:45",
        type = SignalType.SUCCESS,
        icon = Icons.Default.CheckCircle,
        title = "Система в норме",
        description = "Все параметры в пределах нормы",
        location = ""
    )
)
