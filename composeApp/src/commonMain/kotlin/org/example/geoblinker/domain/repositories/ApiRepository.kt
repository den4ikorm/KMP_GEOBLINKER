package org.example.geoblinker.domain.repositories


import org.example.geoblinker.domain.models.*


interface ApiRepository {


    suspend fun register( request: Map<String, String> ): Code
    suspend fun auth( request: Map<String, String> ): Authorization
    suspend fun getToken( hash: String ): Token
    suspend fun getHash( token: String ): Token
    suspend fun editUserData( userName: String, request: Map<String, String> ): Code
    suspend fun editUser( request: Map<String, String> ): Code
    suspend fun addCar( request: Map<String, String>): ResponseCreateCar
    suspend fun getAllCar( request: Map<String, String> ): Cars
    suspend fun updateCar( cId: String, request: Map<String, String> ): Code
    suspend fun sendEmailTechSupport( request: Map<String, String> ): Code
    suspend fun createSubscription( request: Map<String, String> ): SubscriptionResponse
    suspend fun getSubscription( request: Map<String, String> ): SubscriptionListResponse
    suspend fun createPayment( request: Map<String, String> ): PaymentResponse
    suspend fun getPayment( request: Map<String, String> ): PaymentInfoResponse
    suspend fun getTariffs(): TariffResponseMap
    suspend fun getLangData( langId: String): DataLangResponse
    suspend fun getDeviceSignalsData( s:String ="" ): DataResponse


