package org.example.geoblinker.presentation.features.map_screen


import androidx.compose.material3.*
import androidx.compose.runtime.*


interface MapController {
    fun evaluateJavascript(script: String)
    fun setCenter(lat: Double, lng: Double)
    fun addMarker(id: String, lat: Double, lng: Double, iconName: String, w: Int, h: Int)
    fun removeMarker(id: String)
    fun clearLayers()
    fun zoomIn()
    fun zoomOut()
    fun switchTheme(theme: String)
