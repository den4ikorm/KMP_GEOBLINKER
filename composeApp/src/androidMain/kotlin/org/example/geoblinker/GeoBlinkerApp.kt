package org.example.geoblinker

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.example.geoblinker.di.appModule

class GeoBlinkerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GeoBlinkerApp)
            modules(appModule)
        }
    }
}
