package org.example.geoblinker.di


import org.example.geoblinker.domain.usecases.*
import org.koin.dsl.module


/**
 * Domain модуль DI
 * Содержит use cases и бизнес-логику
 */
val domainModule = module {
    
    
    // Auth Use Cases
    single { RegisterUserUseCase(get()) }
    single { AuthenticateUserUseCase(get()) }
    single { GetTokenUseCase(get()) }
    
    
    // User Profile Use Cases
    single { UpdateUserProfileUseCase(get()) }
    
    
    // Car Management Use Cases
    single { ManageCarsUseCase(get()) }
    
    
    // Subscription Use Cases
    single { ManageSubscriptionUseCase(get()) }
    
    
    // Payment Use Cases
    single { ManagePaymentUseCase(get()) }
    
    
    // Other Use Cases
    single { GetTariffsUseCase(get()) }
    single { SendSupportEmailUseCase(get()) }
    single { GetDeviceSignalsUseCase(get()) }
    
    
    // Device Use Cases
    single { SyncDevicesUseCase(get(), get()) }
    single { CheckImeiUseCase(get()) }
}
