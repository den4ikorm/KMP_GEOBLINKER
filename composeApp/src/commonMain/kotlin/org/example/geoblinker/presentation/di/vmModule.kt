package org.example.geoblinker.presentation.di


import org.example.geoblinker.presentation.features.auth.AuthViewModel
import org.example.geoblinker.presentation.viewmodels.*
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * Module для всех ViewModels приложения
 * Обновлено: все ViewModels зарегистрированы с правильными зависимостями
 */
val vmModule = module {
    // Auth
    viewModel { AuthViewModel(get(), get()) }
    
    
    // Device
    viewModel { DeviceViewModel(get(), get(), get()) }
    
    
    // Map
    viewModel { MapViewModel(get(), get()) }
    
    
    // Profile (ИСПРАВЛЕНО - добавлена зависимость)
    viewModel { ProfileViewModel(get()) }
    
    
    // Subscription
    viewModel { SubscriptionViewModel(get()) }
    
    
    // Notification
    viewModel { NotificationViewModel(get()) }
}
