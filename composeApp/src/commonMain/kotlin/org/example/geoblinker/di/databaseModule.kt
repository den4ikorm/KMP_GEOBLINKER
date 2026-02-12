package org.example.geoblinker.di


import org.example.geoblinker.core.database.DatabaseDriverFactory
import org.example.geoblinker.database.AppDatabase
import org.koin.dsl.module


val databaseModule = module {
    single { DatabaseDriverFactory(get()) }
    single { AppDatabase(get<DatabaseDriverFactory>().createDriver()) }
}
