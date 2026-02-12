package org.example.geoblinker

import org.example.geoblinker.di.coreModule
import org.example.geoblinker.di.dataModule
import org.example.geoblinker.di.domainModule
import org.example.geoblinker.di.viewModelModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Initializes Koin DI for iOS
 * 
 * Called from SwiftUI app initialization
 * Sets up all DI modules for the app
 */
fun initKoin() {
    startKoin {
        modules(
            coreModule,
            dataModule,
            domainModule,
            viewModelModule,
            iosModule()
        )
    }
}

/**
 * iOS-specific Koin module
 * 
 * Provides platform-specific dependencies
 */
fun iosModule(): Module = module {
    // iOS-specific dependencies can be added here
    // For example: database driver factory
}
