package org.example.geoblinker.di


import org.example.geoblinker.core.network.NetworkClient
import org.example.geoblinker.core.session.SessionManager
import org.koin.dsl.module


/**
 * Core модуль DI
 * Содержит базовые зависимости: network, session, utils
 */
val coreModule = module {
    
    
    // Network Client (Ktor)
    single { NetworkClient.create() }
    
    
    // Session Manager
    single { SessionManager() }
}
