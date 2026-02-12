package org.example.geoblinker.presentation.features.map_screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.geoblinker.ui.*
import geoblinker.composeapp.generated.resources.*
import geoblinker.composeapp.generated.resources.Res
import org.example.geoblinker.domain.models.Devices
import org.example.geoblinker.presentation.features.map_screen.widgets.CustomDevicePopup
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel


val Int.sdp get() = this.dp


/**
 * MapScreen - ТОЛЬКО UI
 * Вся логика в MapViewModel!
 */
@Composable
fun MapScreen(
    viewModel: MapViewModel = koinViewModel(),
    toBindingScreen: () -> Unit,
    toDeviceScreen: (Devices) -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    
    // Обработка эффектов (side-effects)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MapEffect.NavigateToBinding -> toBindingScreen()
                is MapEffect.NavigateToDeviceDetails -> toDeviceScreen(effect.device)
                is MapEffect.RequestLocationPermission -> {
                    // Platform-specific обработка
                    // На Android/iOS будет реализовано через expect/actual
                }
                else -> {} // ZoomToDevice, ShowError обрабатываются в UI
            }
        }
    }
    
    
    Box(Modifier.fillMaxSize()) {
        // Карта (platform-specific компонент)
        WebViewMap(
            modifier = Modifier.fillMaxSize(),
            state = state,
            onDeviceClick = { deviceId ->
                val device = state.devices.find { it.imei == deviceId }
                viewModel.onEvent(MapEvent.OnDeviceSelected(device))
            }
        )
        
        
        // Кнопки управления картой
        MapControls(
            state = state,
            onEvent = viewModel::onEvent
        )
        
        
        // Попап выбранного устройства
        if (state.selectedDevice != null) {
            CustomDevicePopup(
                device = state.selectedDevice!!,
                onDismiss = { 
                    viewModel.onEvent(MapEvent.OnDeviceSelected(null))
                },
                onDeviceClick = { device ->
                    toDeviceScreen(device)
                }
            )
        }
        
        
        // Попап поиска
        if (state.showSearchPopup) {
            SearchDialog(
                onDismiss = {
                    viewModel.onEvent(MapEvent.OnSearchPopupToggle)
                },
                onSearch = { query ->
                    viewModel.onEvent(MapEvent.OnSearchDevice(query))
                }
            )
        }
        
        
        // Попап пустых устройств
        if (state.showEmptyDevicesPopup) {
            CustomEmptyDevicesPopup(
                onClose = { 
                    viewModel.onEvent(MapEvent.OnEmptyDevicesPopupDismiss)
                },
                onAdd = {
                    viewModel.onEvent(MapEvent.OnNavigateToBinding)
                }
            )
        }
    }
}


/**
 * Кнопки управления картой
 * Отдельный компонент для чистоты
 */
@Composable
private fun BoxScope.MapControls(
    state: MapState,
    onEvent: (MapEvent) -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .align(Alignment.CenterEnd),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier.padding(end = 16.dp)
        ) {
            HSpacer(66)
            
            
            // Поиск
            MapControlButton(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF373736), Color(0xFF212120))
                ),
                onClick = { onEvent(MapEvent.OnSearchPopupToggle) }
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.magnifier),
                    contentDescription = "Search",
                    modifier = Modifier.size(24.sdp),
                    tint = Color.Unspecified
                )
            }
            HSpacer(30)
            
            
            // Zoom In
            MapControlButton(
                color = Color.White,
                onClick = { onEvent(MapEvent.OnZoomIn) }
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.plus_thin),
                    contentDescription = "Zoom In",
                    modifier = Modifier.size(14.sdp),
                    tint = Color.Unspecified
                )
            }
            HSpacer(15)
            
            
            // Zoom Out
            MapControlButton(
                color = Color.White,
                onClick = { onEvent(MapEvent.OnZoomOut) }
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.minus_thin),
                    contentDescription = "Zoom Out",
                    modifier = Modifier.size(14.sdp),
                    tint = Color.Unspecified
                )
            }
            HSpacer(30)
            
            
            // Темная тема
            Icon(
                painter = painterResource(Res.drawable.theme),
                contentDescription = "Theme",
                modifier = Modifier
                    .size(40.sdp)
                    .clickable {
                        val newTheme = when (state.mapTheme) {
                            MapTheme.LIGHT -> MapTheme.DARK
                            MapTheme.DARK -> MapTheme.SATELLITE
                            MapTheme.SATELLITE -> MapTheme.LIGHT
                        }
                        onEvent(MapEvent.OnThemeChanged(newTheme))
                    },
                tint = Color.Unspecified
            )
            HSpacer(15)
            
            
            // Спутник
            Icon(
                painter = painterResource(Res.drawable.sputnik),
                contentDescription = "Satellite",
                modifier = Modifier
                    .size(40.sdp)
                    .clickable {
                        val newTheme = if (state.mapTheme == MapTheme.SATELLITE) {
                            MapTheme.LIGHT
                        } else {
                            MapTheme.SATELLITE
                        }
                        onEvent(MapEvent.OnThemeChanged(newTheme))
                    },
                tint = Color.Unspecified
            )
            HSpacer(30)
            
            
            // Моя локация
            MapControlButton(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF373736), Color(0xFF212120))
                ),
                shape = CircleShape,
                onClick = { onEvent(MapEvent.OnRequestLocation) }
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = "My Location",
                    modifier = Modifier.size(24.sdp),
                    tint = Color.White
                )
            }
        }
    }
}


/**
 * Диалог поиска
 */
@Composable
private fun SearchDialog(
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Поиск устройства") },
        text = {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Введите имя или IMEI") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { 
                onSearch(searchText)
            }) {
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
