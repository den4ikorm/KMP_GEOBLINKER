package org.example.geoblinker

import androidx.compose.ui.window.ComposeUIViewController
import org.example.geoblinker.presentation.navigation.NavGraph
import org.example.geoblinker.presentation.theme.GeoBlinkerTheme
import platform.UIKit.UIViewController

/**
 * Main iOS ViewController for Compose UI
 * 
 * This function creates a UIViewController that hosts the Compose UI
 * It's called from SwiftUI's ContentView
 */
fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        GeoBlinkerTheme {
            NavGraph()
        }
    }
}
