package org.example.geoblinker.presentation.features.map_screen

import androidx.compose.runtime.*
import platform.CoreLocation.*
import platform.Foundation.NSError
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents

/**
 * iOS LocationHandler using CoreLocation
 * 
 * Uses CLLocationManager to request and receive location updates
 * Handles permission requests automatically
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun LocationHandler(
    requestLocation: Boolean,
    onLocationReceived: (Double, Double) -> Unit,
    onPermissionDenied: () -> Unit
) {
    val locationManager = remember { CLLocationManager() }
    
    DisposableEffect(requestLocation) {
        if (requestLocation) {
            // Create delegate for location updates
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(
                    manager: CLLocationManager,
                    didUpdateLocations: List<*>
                ) {
                    @Suppress("UNCHECKED_CAST")
                    val locations = didUpdateLocations as List<CLLocation>
                    locations.lastOrNull()?.coordinate?.useContents {
                        onLocationReceived(latitude, longitude)
                    }
                }
                
                override fun locationManager(
                    manager: CLLocationManager,
                    didFailWithError: NSError
                ) {
                    println("Location error: ${didFailWithError.localizedDescription}")
                    onPermissionDenied()
                }
                
                override fun locationManagerDidChangeAuthorization(
                    manager: CLLocationManager
                ) {
                    when (manager.authorizationStatus) {
                        kCLAuthorizationStatusAuthorizedWhenInUse,
                        kCLAuthorizationStatusAuthorizedAlways -> {
                            manager.startUpdatingLocation()
                        }
                        kCLAuthorizationStatusDenied,
                        kCLAuthorizationStatusRestricted -> {
                            onPermissionDenied()
                        }
                        else -> {
                            // Request authorization
                            manager.requestWhenInUseAuthorization()
                        }
                    }
                }
            }
            
            locationManager.delegate = delegate
            
            // Check current authorization status
            when (locationManager.authorizationStatus) {
                kCLAuthorizationStatusNotDetermined -> {
                    locationManager.requestWhenInUseAuthorization()
                }
                kCLAuthorizationStatusAuthorizedWhenInUse,
                kCLAuthorizationStatusAuthorizedAlways -> {
                    locationManager.startUpdatingLocation()
                }
                kCLAuthorizationStatusDenied,
                kCLAuthorizationStatusRestricted -> {
                    onPermissionDenied()
                }
                else -> {}
            }
        }
        
        onDispose {
            locationManager.stopUpdatingLocation()
        }
    }
}
