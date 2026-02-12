package org.example.geoblinker.presentation.features.map_screen

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*
import platform.WebKit.*
import platform.darwin.NSObject

/**
 * iOS WebViewMap implementation using WKWebView and Leaflet (OpenStreetMap)
 * 
 * This provides a cross-platform map solution for iOS without Google Maps dependency.
 * Uses Leaflet.js with OpenStreetMap tiles for iOS compatibility.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebViewMap(
    modifier: Modifier,
    onMapReady: (MapController) -> Unit,
    onMarkerClick: (String) -> Unit
) {
    var webView: WKWebView? by remember { mutableStateOf(null) }
    var isMapReady by remember { mutableStateOf(false) }
    
    val mapController = remember {
        object : MapController {
            override fun addMarker(lat: Double, lng: Double, title: String, id: String) {
                webView?.evaluateJavaScript(
                    """
                    addMarker($lat, $lng, "$title", "$id");
                    """.trimIndent()
                ) { _, _ -> }
            }
            
            override fun removeMarker(markerId: String) {
                webView?.evaluateJavaScript(
                    """
                    removeMarker("$markerId");
                    """.trimIndent()
                ) { _, _ -> }
            }
            
            override fun moveCamera(lat: Double, lng: Double, zoom: Float) {
                webView?.evaluateJavaScript(
                    """
                    moveCamera($lat, $lng, $zoom);
                    """.trimIndent()
                ) { _, _ -> }
            }
            
            override fun clearMarkers() {
                webView?.evaluateJavaScript("clearMarkers();") { _, _ -> }
            }
        }
    }
    
    LaunchedEffect(isMapReady) {
        if (isMapReady) {
            onMapReady(mapController)
        }
    }
    
    UIKitView(
        factory = {
            val config = WKWebViewConfiguration()
            val contentController = WKUserContentController()
            
            // Add script message handler for map callbacks
            val messageHandler = object : NSObject(), WKScriptMessageHandlerProtocol {
                override fun userContentController(
                    userContentController: WKUserContentController,
                    didReceiveScriptMessage: WKScriptMessage
                ) {
                    when (didReceiveScriptMessage.name) {
                        "onMapReady" -> {
                            isMapReady = true
                        }
                        "onMarkerClick" -> {
                            val markerId = didReceiveScriptMessage.body as? String ?: ""
                            onMarkerClick(markerId)
                        }
                    }
                }
            }
            
            contentController.addScriptMessageHandler(messageHandler, "onMapReady")
            contentController.addScriptMessageHandler(messageHandler, "onMarkerClick")
            
            config.userContentController = contentController
            
            WKWebView(frame = platform.CoreGraphics.CGRectZero, configuration = config).apply {
                loadHTMLString(getLeafletHTML(), null)
                webView = this
            }
        },
        modifier = modifier
    )
}

/**
 * Generates Leaflet map HTML
 * Uses OpenStreetMap tiles (free, no API key required)
 */
private fun getLeafletHTML(): String = """
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <style>
        body {
            margin: 0;
            padding: 0;
        }
        #map {
            width: 100vw;
            height: 100vh;
        }
    </style>
</head>
<body>
    <div id="map"></div>
    <script>
        // Initialize map
        const map = L.map('map').setView([55.7558, 37.6173], 13); // Default: Moscow
        
        // Add OpenStreetMap tiles
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; OpenStreetMap contributors',
            maxZoom: 19
        }).addTo(map);
        
        // Marker storage
        const markers = {};
        
        // Add marker function
        function addMarker(lat, lng, title, id) {
            // Remove existing marker if exists
            if (markers[id]) {
                map.removeLayer(markers[id]);
            }
            
            // Create new marker
            const marker = L.marker([lat, lng])
                .bindPopup(title)
                .addTo(map);
            
            marker.on('click', function() {
                window.webkit.messageHandlers.onMarkerClick.postMessage(id);
            });
            
            markers[id] = marker;
        }
        
        // Remove marker function
        function removeMarker(id) {
            if (markers[id]) {
                map.removeLayer(markers[id]);
                delete markers[id];
            }
        }
        
        // Move camera function
        function moveCamera(lat, lng, zoom) {
            map.setView([lat, lng], zoom);
        }
        
        // Clear all markers
        function clearMarkers() {
            Object.keys(markers).forEach(id => {
                map.removeLayer(markers[id]);
            });
            markers = {};
        }
        
        // Notify iOS that map is ready
        setTimeout(function() {
            window.webkit.messageHandlers.onMapReady.postMessage('ready');
        }, 500);
    </script>
</body>
</html>
""".trimIndent()
