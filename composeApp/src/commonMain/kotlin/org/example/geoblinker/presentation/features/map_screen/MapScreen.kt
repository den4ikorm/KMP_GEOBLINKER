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
import com.example.geoblinker.ui.*
import geoblinker.composeapp.generated.resources.*
import geoblinker.composeapp.generated.resources.Res
import kotlinx.coroutines.delay
import org.example.geoblinker.domain.models.Devices
import org.example.geoblinker.presentation.features.map_screen.widgets.CustomDevicePopup
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


// commonMain/src/.../presentation/features/map_screen/MapScreen.kt






val Int.sdp get() = this.dp


@Composable
fun MapScreen(
    viewModel: DeviceViewModel = koinViewModel(),
    toBindingScreen: () -> Unit,
    toDeviceScreen: (Devices) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val devices = state.devices
    val selectedMarker = state.selectedMarker


    var mapController by remember { mutableStateOf<MapController?>(null) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isSatellite by remember { mutableStateOf(false) }
    var isShowPopup by remember { mutableStateOf(false) }
    var isShowPopupSearch by remember { mutableStateOf(false) }
    var dontSearch by remember { mutableStateOf(false) }


    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var triggerLocationRequest by remember { mutableStateOf(false) }


    val scaleIcons = 1.0


    LocationHandler(
        requestLocation = triggerLocationRequest,
        onLocationReceived = { lat, lng ->
            currentLocation = lat to lng
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


    LaunchedEffect(Unit) {
        delay(2000)
        if (devices.isEmpty()) {
            isShowPopup = true
        }
        // Запрашиваем локацию при старте
        triggerLocationRequest = true
    }


    LaunchedEffect(devices, mapController) {
        val controller = mapController ?: return@LaunchedEffect
        while (true) {
            devices.forEach { item ->
                if (item.isConnected && item.lat != -999999999.9 && item.lng != -999999999.9) {
                    val filename = "car_marker.svg" // MarkersRepository.getFilename(item.markerId)


                    controller.addMarker(
                        item.imei, item.lat, item.lng, filename,
                        (32 * scaleIcons).toInt(), (32 * scaleIcons).toInt()
                    )
                } else {
                    controller.removeMarker(item.imei)
                }
            }
            delay(5000)
        }
    }




    Box(Modifier.fillMaxSize()) {


        WebViewMap(
            modifier = Modifier.fillMaxSize(),
            onMapReady = { controller -> mapController = controller },
            onMarkerClick = { markerId ->
                // Находим девайс по ID (IMEI) и устанавливаем как выбранный
                val device = devices.find { it.imei == markerId }
                viewModel.setSelectedMarker(device) // Метод должен быть публичным в VM
            }
        )


        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column {
                HSpacer(66)
                // Поиск
                MapControlButton(
                    brush = Brush.verticalGradient(listOf(Color(0xFF373736), Color(0xFF212120))),
                    onClick = { isShowPopupSearch = true }
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.magnifier),
                        contentDescription = null,
                        modifier = Modifier.size(24.sdp),
                        tint = Color.Unspecified
                    )
                }
                HSpacer(30)


                MapControlButton(color = Color.White, onClick = { mapController?.zoomIn() }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.plus_thin),
                        contentDescription = null,
                        modifier = Modifier.size(14.sdp),
                        tint = Color.Unspecified
                    )
                }
                HSpacer(15)


                MapControlButton(color = Color.White, onClick = { mapController?.zoomOut() }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.minus_thin),
                        contentDescription = null,
                        modifier = Modifier.size(14.sdp),
                        tint = Color.Unspecified
                    )
                }
                HSpacer(30)


                Icon(
                    painter = painterResource(Res.drawable.theme),
                    contentDescription = null,
                    modifier = Modifier.size(40.sdp).clickable {
                        isDarkTheme = !isDarkTheme
                        val theme = if(isSatellite) "satellite" else if(isDarkTheme) "dark" else "light"
                        mapController?.switchTheme(theme)
                    },
                    tint = Color.Unspecified
                )
                HSpacer(15)


                Icon(
                    painter = painterResource(Res.drawable.sputnik),
                    contentDescription = null,
                    modifier = Modifier.size(40.sdp).clickable {
                        isSatellite = !isSatellite
                        val theme = if(isSatellite) "satellite" else if(isDarkTheme) "dark" else "light"
                        mapController?.switchTheme(theme)
                    },
                    tint = Color.Unspecified
                )
                HSpacer(30)


                MapControlButton(
                    brush = Brush.verticalGradient(listOf(Color(0xFF373736), Color(0xFF212120))),
                    shape = CircleShape,
                    onClick = { triggerLocationRequest = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MyLocation,
                        contentDescription = null,
                        modifier = Modifier.size(24.sdp),
                        tint = Color.White
                    )
                }
            }
        }


        if (isShowPopupSearch) {
            SearchDialog(
                onDismiss = {
                    isShowPopupSearch = false
                    dontSearch = false
                },
                dontSearch = dontSearch,
                onSearch = { key ->
                    // Логика поиска
                    val found = devices.find { (it.imei.contains(key) || it.name.contains(key)) && it.isConnected }
                    if (found == null) dontSearch = true
                    else {
                        isShowPopupSearch = false
                        viewModel.setSelectedMarker(found)
                    }
                },
                onResetSearch = { dontSearch = false }
            )
        }


        CustomDevicePopup(
            viewModel = viewModel,
            selectedMarker = selectedMarker,
            mapController = mapController, // Передаем контроллер!
            onDismiss = { viewModel.setSelectedMarker(null) },
            toDeviceScreen = toDeviceScreen
        )


        if (isShowPopup) {
            CustomEmptyDevicesPopup(
                onClose = { isShowPopup = false },
                onAdd = {
                    isShowPopup = false
                    toBindingScreen()
                }
            )
        }
    }
