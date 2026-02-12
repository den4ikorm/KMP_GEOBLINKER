package org.example.geoblinker.core.network


import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


object NetworkClient {
    
    
    fun createMainApiClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
            
            
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
            
            
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
            }
        }
    }
    
    
    fun createImeiApiClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            
            
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
            
            
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
            }
        }
    }
}
