package org.example.geoblinker.presentation.features.map_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.example.geoblinker.core.utils.MarkerUtils
import org.example.geoblinker.domain.models.Devices
import org.example.geoblinker.presentation.features.map_screen.widgets.CustomDevicePopup
import org.example.geoblinker.presentation.viewmodels.MapViewModel
import org.koin.compose.koinInject


val Int.sdp get() = this.dp


/**
 * MapScreen - ОБНОВЛЕННЫЙ с реальными данными
 */
@Composable
fun MapScreenUpdated(
    viewModel: MapViewModel = koinInject(),
    onNavigateToBinding: () -> Unit = {},
    onNavigateToDeviceDetails: (Devices) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val devices = state.devices
    val selectedDevice = state.selectedDevice
    
    
    var mapController by remember { mutableStateOf<MapController?>(null) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showEmptyPopup by remember { mutableStateOf(false) }
    var triggerLocationRequest by remember { mutableStateOf(false) }
    
    
    val scaleIcons = 1.0
    val snackbarHostState = remember { SnackbarHostState() }
    
    
    // Обработка эффектов
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MapEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is MapEffect.ZoomToDevice -> {
                    mapController?.setCenter(effect.device.lat, effect.device.lng)
                    mapController?.setZoom(15)
                }
                is MapEffect.ZoomToLocation -> {
                    mapController?.setCenter(effect.lat, effect.lng)
                }
            }
        }
    }
    
    
    // Обработчик локации пользователя
    LocationHandler(
        requestLocation = triggerLocationRequest,
        onLocationReceived = { lat, lng ->
            viewModel.handleEvent(MapEvent.OnUserLocationUpdated(lat, lng))
            triggerLocationRequest = false
            
            
            mapController?.addMarker(
                "myLocation", lat, lng, "my_marker.svg",
                (26 * scaleIcons).toInt(), (26 * scaleIcons).toInt()
            )
            mapController?.setCenter(lat, lng)
        },
        onPermissionDenied = {
            triggerLocationRequest = false
        }
    )
    
    
    // Показ popup при пустом списке
    LaunchedEffect(devices) {
        delay(2000)
        if (devices.isEmpty()) {
            showEmptyPopup = true
        }
    }
    
    
    // Автозапрос локации
    LaunchedEffect(Unit) {
        delay(1000)
        triggerLocationRequest = true
    }
    
    
    // Обновление маркеров на карте (каждые 5 секунд)
    LaunchedEffect(devices, mapController) {
        val controller = mapController ?: return@LaunchedEffect
        
        
        while (true) {
            devices.forEach { device ->
                if (device.isConnected == 1L && MarkerUtils.isValidCoordinates(device.lat, device.lng)) {
                    val filename = MarkerUtils.getMarkerFilename(device.markerId)
                    
                    
                    controller.addMarker(
                        device.imei,
                        device.lat,
                        device.lng,
                        filename,
                        (32 * scaleIcons).toInt(),
                        (32 * scaleIcons).toInt()
                    )
                } else {
                    controller.removeMarker(device.imei)
                }
            }
            delay(5000) // Обновление каждые 5 секунд
        }
    }
    
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            
            
            // Карта
            WebViewMap(
                modifier = Modifier.fillMaxSize(),
                onMapReady = { controller -> mapController = controller },
                onMarkerClick = { markerId ->
                    val device = devices.find { it.imei == markerId }
                    viewModel.handleEvent(MapEvent.OnDeviceSelected(device))
                }
            )
            
            
            // Контролы карты (справа)
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(
                    modifier = Modifier.padding(end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Поиск
                    MapControlButton(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF373736), Color(0xFF212120))
                        ),
                        onClick = { showSearchDialog = true }
                    ) {
                        Text("🔍", fontSize = 20.sp)
                    }
                    
                    
                    // Zoom In
                    MapControlButton(
                        color = Color.White,
                        onClick = { mapController?.zoomIn() }
                    ) {
                        Text("+", fontSize = 24.sp, color = Color.Black)
                    }
                    
                    
                    // Zoom Out
                    MapControlButton(
                        color = Color.White,
                        onClick = { mapController?.zoomOut() }
                    ) {
                        Text("−", fontSize = 24.sp, color = Color.Black)
                    }
                    
                    
                    // Тема
                    MapControlButton(
                        color = Color.White,
                        onClick = {
                            val currentTheme = state.mapTheme
                            val newTheme = when (currentTheme) {
                                MapTheme.LIGHT -> MapTheme.DARK
                                MapTheme.DARK -> MapTheme.SATELLITE
                                MapTheme.SATELLITE -> MapTheme.LIGHT
                            }
                            viewModel.handleEvent(MapEvent.OnThemeChanged(newTheme))
                            
                            
                            val themeStr = when (newTheme) {
                                MapTheme.LIGHT -> "light"
                                MapTheme.DARK -> "dark"
                                MapTheme.SATELLITE -> "satellite"
                            }
                            mapController?.switchTheme(themeStr)
                        }
                    ) {
                        Text("🌓", fontSize = 20.sp)
                    }
                    
                    
                    // Спутник
                    MapControlButton(
                        color = Color.White,
                        onClick = {
                            viewModel.handleEvent(MapEvent.OnThemeChanged(MapTheme.SATELLITE))
                            mapController?.switchTheme("satellite")
                        }
                    ) {
                        Text("🛰️", fontSize = 20.sp)
                    }
                    
                    
                    // Моя локация
                    MapControlButton(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF373736), Color(0xFF212120))
                        ),
                        shape = CircleShape,
                        onClick = { triggerLocationRequest = true }
                    ) {
                        Text("📍", fontSize = 20.sp)
                    }
                    
                    
                    // Обновить устройства
                    MapControlButton(
                        color = Color(0xFF00E5FF),
                        onClick = { viewModel.handleEvent(MapEvent.OnRefreshDevices) }
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("🔄", fontSize = 20.sp)
                        }
                    }
                }
            }
            
            
            // Popup устройства
            if (selectedDevice != null) {
                CustomDevicePopup(
                    device = selectedDevice,
                    onDismiss = { viewModel.handleEvent(MapEvent.OnDeviceSelected(null)) },
                    onNavigateToDetails = { onNavigateToDeviceDetails(selectedDevice) },
                    onZoomTo = {
                        mapController?.setCenter(selectedDevice.lat, selectedDevice.lng)
                        mapController?.setZoom(15)
                    }
                )
            }
            
            
            // Popup пустого списка
            if (showEmptyPopup) {
                CustomEmptyDevicesPopup(
                    onClose = { showEmptyPopup = false },
                    onAdd = {
                        showEmptyPopup = false
                        onNavigateToBinding()
                    }
                )
            }
            
            
            // Диалог поиска
            if (showSearchDialog) {
                SearchDialog(
                    onDismiss = { showSearchDialog = false },
                    onSearch = { query ->
                        viewModel.handleEvent(MapEvent.OnSearchDevice(query))
                        showSearchDialog = false
                    }
                )
            }
        }
    }
}


/**
 * Кнопка контрола карты
 */
@Composable
private fun MapControlButton(
    modifier: Modifier = Modifier,
    color: Color? = null,
    brush: Brush? = null,
    shape: androidx.compose.ui.graphics.Shape = CircleShape,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(
                brush = brush ?: Brush.linearGradient(listOf(color ?: Color.White, color ?: Color.White)),
                shape = shape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}


/**
 * Диалог поиска устройства
 */
@Composable
private fun SearchDialog(
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Поиск устройства") },
        text = {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Имя, IMEI или номер") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSearch(searchQuery) },
                enabled = searchQuery.isNotBlank()
            ) {
                Text("Найти")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}


/**
 * Popup при пустом списке устройств
 */
@Composable
private fun CustomEmptyDevicesPopup(
    onClose: () -> Unit,
    onAdd: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Нет устройств") },
        text = { Text("У вас пока нет подключенных устройств. Добавьте первое устройство, чтобы начать отслеживание.") },
        confirmButton = {
            TextButton(onClick = onAdd) {
                Text("Добавить устройство")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Закрыть")
            }
        }
    )
}
