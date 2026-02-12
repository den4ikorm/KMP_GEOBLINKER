import SwiftUI

/**
 * GeoBlinker iOS App
 * 
 * Main application entry point for iOS
 * Uses Compose Multiplatform for UI rendering
 */
@main
struct iOSApp: App {
    
    init() {
        // Initialize Koin DI on iOS
        KoinInitializerKt.initKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
