package org.example.geoblinker.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.example.geoblinker.database.AppDatabase

/**
 * iOS-specific SQLDelight database driver factory
 * Uses NativeSqliteDriver for iOS native SQLite support
 */
actual class DatabaseDriverFactory {
    
    /**
     * Creates a native iOS SQLite driver
     * 
     * @return SqlDriver instance for iOS
     */
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = AppDatabase.Schema,
            name = "geoblinker.db"
        )
    }
}

/**
 * Helper function to create database instance on iOS
 */
fun createDatabase(): AppDatabase {
    val driver = DatabaseDriverFactory().createDriver()
    return AppDatabase(driver)
}
