package org.example.geoblinker.presentation.features.objects_list


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.geoblinker.domain.models.Devices
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


/**
 * ObjectsListScreen - ТОЛЬКО UI
 * Вся логика в ObjectsListViewModel!
 */
@Composable
fun ObjectsListScreen(
    viewModel: ObjectsListViewModel = koinViewModel(),
    toBindingScreen: () -> Unit,
    toDeviceScreen: (Devices) -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    
    // Обработка эффектов
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ObjectsListEffect.NavigateToDeviceDetails -> {
                    toDeviceScreen(effect.device)
                }
                is ObjectsListEffect.NavigateToBinding -> {
                    toBindingScreen()
                }
                is ObjectsListEffect.RequestLocationPermission -> {
                    // Platform-specific обработка
                }
                is ObjectsListEffect.ShowError -> {
                    // Показать Snackbar
                }
            }
        }
    }
    
    
    // Основной контент
    Box(Modifier.fillMaxSize()) {
        if (state.filteredDevices.isEmpty() && state.disconnectedDevices.isEmpty()) {
            EmptyStateContent()
        } else {
            ListContent(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
        
        
        // Кнопка добавления устройства
        FloatingActionButton(
            onClick = { viewModel.onEvent(ObjectsListEvent.OnNavigateToBinding) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            containerColor = Color(0xFF12CD4A)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Device",
                    tint = Color.White
                )
                Text(
                    "Привязать устройство",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


/**
 * Пустое состояние
 */
@Composable
private fun EmptyStateContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Нет активных устройств",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}


/**
 * Контент списка
 */
@Composable
private fun ListContent(
    state: ObjectsListState,
    onEvent: (ObjectsListEvent) -> Unit
) {
    var showSortDialog by remember { mutableStateOf(false) }
    
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поиск
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { query ->
                onEvent(ObjectsListEvent.OnSearchQueryChanged(query))
            }
        )
        
        
        Spacer(Modifier.height(25.dp))
        
        
        // Сортировка
        SortButton(
            sortType = state.sortType,
            onClick = { showSortDialog = true }
        )
        
        
        // Список устройств
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // Подключенные устройства
            items(state.filteredDevices) { device ->
                DeviceCard(
                    device = device,
                    onClick = { onEvent(ObjectsListEvent.OnDeviceClick(device)) }
                )
            }
            
            
            // Отключенные устройства
            items(state.disconnectedDevices) { device ->
                DisconnectedDeviceCard(device = device)
            }
            
            
            // Отступ для кнопки
            item {
                Spacer(Modifier.height(100.dp))
            }
        }
    }
    
    
    // Диалог сортировки
    if (showSortDialog) {
        SortDialog(
            currentSortType = state.sortType,
            hasLocation = state.userLocation != null,
            onSortTypeSelected = { sortType ->
                onEvent(ObjectsListEvent.OnSortTypeChanged(sortType))
                showSortDialog = false
            },
            onDismiss = { showSortDialog = false }
        )
    }
}


/**
 * Поисковая строка
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Поиск по имени или IMEI") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF5F5F5),
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}


/**
 * Кнопка сортировки
 */
@Composable
private fun SortButton(
    sortType: SortType,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = getSortTypeName(sortType),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.width(16.dp))
        Text("▼", style = MaterialTheme.typography.bodySmall)
    }
}


/**
 * Карточка подключенного устройства
 */
@Composable
private fun DeviceCard(
    device: Devices,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFDAD9D9).copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Индикатор подключения
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .background(
                            Color(0xFF12CD4A),
                            RoundedCornerShape(2.dp)
                        )
                )
                
                
                Spacer(Modifier.width(11.dp))
                
                
                // Название устройства
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            
            Spacer(Modifier.height(8.dp))
            
            
            // IMEI
            Text(
                text = "IMEI: ${device.imei}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF737373)
            )
            
            
            // Дополнительная информация
            if (device.registrationPlate.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Номер: ${device.registrationPlate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF737373)
                )
            }
        }
    }
}


/**
 * Карточка отключенного устройства
 */
@Composable
private fun DisconnectedDeviceCard(device: Devices) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .background(
                        Color(0xFFC4162D),
                        RoundedCornerShape(2.dp)
                    )
            )
            
            
            Spacer(Modifier.width(11.dp))
            
            
            Text(
                text = "IMEI: ${device.imei}",
                color = Color(0xFF737373),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


/**
 * Диалог выбора сортировки
 */
@Composable
private fun SortDialog(
    currentSortType: SortType,
    hasLocation: Boolean,
    onSortTypeSelected: (SortType) -> Unit,
    onDismiss: () -> Unit
) {
    val sortTypes = if (hasLocation) {
        SortType.entries
    } else {
        SortType.entries.filter { it != SortType.BY_DISTANCE }
    }
    
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Сортировка устройств") },
        text = {
            Column {
                sortTypes.forEach { sortType ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSortTypeSelected(sortType) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = sortType == currentSortType,
                            onClick = { onSortTypeSelected(sortType) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(getSortTypeName(sortType))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}


/**
 * Получение названия типа сортировки
 */
private fun getSortTypeName(sortType: SortType): String {
    return when (sortType) {
        SortType.BY_NAME -> "По имени"
        SortType.BY_DEVICE_TYPE -> "По типу устройства"
        SortType.BY_DISTANCE -> "По расстоянию"
        SortType.BY_BINDING_DATE -> "По дате привязки"
        SortType.BY_SIGNAL_STRENGTH -> "По уровню сигнала"
        SortType.BY_CHARGE_LEVEL -> "По уровню заряда"
    }
}
