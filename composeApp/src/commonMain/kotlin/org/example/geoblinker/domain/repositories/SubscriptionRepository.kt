package org.example.geoblinker.domain.repositories


import org.example.geoblinker.domain.models.*


interface SubscriptionRepository {


    suspend fun createSubscription(tariffId: String): Result<String>
    suspend fun createPayment(amount: Int, subsId: String? = null, appUrl: String? = null): Result<PaymentResponseData>
    suspend fun getPaymentStatus(paymentId: String): Result<PaymentInfo>
    suspend fun getUserSubscriptions(): Result<List<SubscriptionInfo>>
    suspend fun getTariffs(): Result<Map<String, Map<String,Any>>>


