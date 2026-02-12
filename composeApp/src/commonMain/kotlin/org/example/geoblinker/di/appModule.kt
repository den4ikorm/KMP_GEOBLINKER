package org.example.geoblinker.di


import org.koin.dsl.module


/**
 * Главный модуль приложения
 * Объединяет все модули Koin
 */
val appModule = module {
    includes(
        coreModule,      // Сеть, платформа, сессии
        databaseModule,  // SQLDelight база данных
        dataModule,      // Репозитории
        domainModule,    // Use Cases
        viewModelModule  // ViewModels
    )
}
