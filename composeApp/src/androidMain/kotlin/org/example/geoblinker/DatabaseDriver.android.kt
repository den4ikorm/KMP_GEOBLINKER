package org.example.geoblinker.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.example.geoblinker.database.AppDatabase

/**
 * Android-specific SQLDelight database driver factory
 * Uses AndroidSqliteDriver for Android SQLite support
 */
actual class DatabaseDriverFactory(private val context: Context) {
    
    /**
     * Creates an Android SQLite driver
     * 
     * @return SqlDriver instance for Android
     */
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "geoblinker.db"
        )
    }
}
