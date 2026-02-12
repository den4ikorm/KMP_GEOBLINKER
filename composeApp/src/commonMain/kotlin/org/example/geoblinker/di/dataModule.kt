package org.example.geoblinker.di


import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.example.geoblinker.data.repositories.*
import org.example.geoblinker.domain.repositories.*
import org.koin.dsl.module


/**
 * Модуль для Data Layer
 * Содержит репозитории и источники данных
 */
val dataModule = module {
    
    
    // Settings (платформозависимое хранилище)
    single { Settings() }
    
    
    // JSON для сериализации
    single {
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    
    
    // Репозитории
    single<SubscriptionRepository> {
        SubscriptionRepositoryImpl(
            api = get(),
            settings = get(),
            database = get(), // Добавлена зависимость от AppDatabase
            json = get()
        )
    }
    
    
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get(), get()) }
    single<TechSupportRepository> { TechSupportRepositoryImpl(get(), get()) }
    single<LangRepository> { LangRepositoryImpl(get(), get()) }
    single<Repository> { RepositoryImpl(get(), get()) }
}
