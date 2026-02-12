package org.example.geoblinker.presentation.features.device_list_signal


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
import org.koin.compose.koinInject


/**
 * Экран истории сигналов устройства
 * 
 * РЕФАКТОРИРОВАНО:
 * - ✅ Вся логика в ViewModel
 * - ✅ UI только отображение
 * - ✅ Нет remember с логикой
 * - ✅ Нет форматирования данных
 * - ✅ Нет фильтрации/группировки
 * - ✅ Side-effects через Effect
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListSignalScreen(
    deviceId: String,
    onNavigateBack: () -> Unit,
    viewModel: DeviceListSignalViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    
    
    // Загрузка данных при первом отображении
    LaunchedEffect(deviceId) {
        viewModel.onEvent(DeviceListSignalEvent.LoadSignals(deviceId))
    }
    
    
    // Обработка side-effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DeviceListSignalEffect.NavigateBack -> onNavigateBack()
                is DeviceListSignalEffect.ShowError -> {
                    // TODO: показать снекбар с ошибкой
                }
                is DeviceListSignalEffect.ShowMessage -> {
                    // TODO: показать снекбар с сообщением
                }
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История сигналов") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(DeviceListSignalEvent.NavigateBack) }) {
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
            FilterTabs(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { filter ->
                    viewModel.onEvent(DeviceListSignalEvent.ChangeFilter(filter))
                }
            )


            // Список сигналов
            when {
                state.isLoading -> {
                    LoadingState()
                }
                state.groupedSignals.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    SignalsList(
                        groupedSignals = state.groupedSignals,
                        isRefreshing = state.isRefreshing,
                        onRefresh = { viewModel.onEvent(DeviceListSignalEvent.Refresh) }
                    )
                }
            }
        }
    }
}


@Composable
private fun FilterTabs(
    selectedFilter: SignalFilter,
    onFilterSelected: (SignalFilter) -> Unit
) {
    val filters = SignalFilter.entries
    
    
    ScrollableTabRow(
        selectedTabIndex = filters.indexOf(selectedFilter),
        modifier = Modifier.fillMaxWidth(),
        edgePadding = 16.dp
    ) {
        filters.forEach { filter ->
            Tab(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                text = { Text(filter.displayName) }
            )
        }
    }
}


@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
private fun EmptyState() {
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
}


@Composable
private fun SignalsList(
    groupedSignals: Map<String, List<SignalItem>>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        groupedSignals.forEach { (date, signals) ->
            item(key = "header_$date") {
                Text(
                    text = date,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }


            items(
                items = signals,
                key = { it.id }
            ) { signal ->
                SignalCard(signal = signal)
            }
        }
    }
}


@Composable
private fun SignalCard(signal: SignalItem) {
    val (icon, iconColor) = getIconAndColor(signal.iconType)


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
                Box(contentAlignment = Alignment.Center) {
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
                        text = signal.timeFormatted,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                
                
                Text(
                    text = signal.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                
                // Дополнительная информация
                if (signal.locationText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DetailChip(
                            icon = Icons.Default.LocationOn,
                            text = signal.locationText
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


@Composable
private fun getIconAndColor(iconType: SignalIconType): Pair<ImageVector, Color> {
    return when (iconType) {
        SignalIconType.Location -> Icons.Default.LocationOn to Color(0xFF4CAF50)
        SignalIconType.Alert -> Icons.Default.Warning to Color(0xFFF44336)
        SignalIconType.Battery -> Icons.Default.BatteryAlert to Color(0xFFFF9800)
        SignalIconType.Speed -> Icons.Default.Speed to Color(0xFF2196F3)
        SignalIconType.Info -> Icons.Default.Info to MaterialTheme.colorScheme.primary
    }
}
