package org.example.geoblinker.presentation.features.map_screen.widgets


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import org.example.geoblinker.presentation.features.map_screen.CustomDevicePopup


@Composable
fun MapFromAssets(
    webView: WebView,
    viewModel: DeviceViewModel,
    toDeviceScreen: (Device) -> Unit
) {
    val addWidth = 30.sdp()
    val addHeight = 45.sdp()




    val devices by viewModel.devices.collectAsState()
    var selectedMarker by remember { mutableStateOf<Device?>(null) }


    AndroidView(
        factory = { _ ->
            webView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                addJavascriptInterface(
                    WebAppInterface { markerId ->
                        selectedMarker = devices.filter { it.imei == markerId }[0]
                    },
                    "AndroidInterface"
                )
                webViewClient = WebViewClient()
                loadUrl("file:///android_asset/map.html")
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .layout { measurable, constraints ->
                // Игнорируем padding родителя
                val looseConstraints = constraints.copy(
                    maxWidth = constraints.maxWidth + addWidth.roundToPx(),
                    maxHeight = constraints.maxHeight + addHeight.roundToPx() * 2
                )
                val placeable = measurable.measure(looseConstraints)
                layout(placeable.width, placeable.height) {
                    placeable.place(0, -addHeight.roundToPx()) // Смещение
                }
            }
    )


    CustomDevicePopup(
        viewModel,
        selectedMarker,
        webView,
        { selectedMarker = null },
        toDeviceScreen
    )
