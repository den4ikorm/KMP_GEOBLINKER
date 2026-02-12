package org.example.geoblinker.data.repositories


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.geoblinker.database.AppDatabase
import org.example.geoblinker.domain.models.*
import org.example.geoblinker.domain.models.PaymentInfo
import org.example.geoblinker.domain.models.PaymentResponseData
import org.example.geoblinker.domain.models.SubscriptionInfo
import org.example.geoblinker.domain.repositories.ApiRepository
import org.example.geoblinker.domain.repositories.SubscriptionRepository


class SubscriptionRepositoryImpl(
    private val api: ApiRepository,
    private val settings: Settings,
    private val database: AppDatabase,
    private val json: Json = Json { encodeDefaults = true; ignoreUnknownKeys = true }
) : SubscriptionRepository {


    override suspend fun createSubscription(tariffId: String): Result<String> = withContext(Dispatchers.Default) {
        try {
            val token = settings.getString("token", "")
            val uHash = settings.getString("hash", "")


            if (token.isEmpty() || uHash.isEmpty()) {
                return@withContext Result.failure(Exception("No authentication tokens"))
            }


            val subscriptionData = SubscriptionData(
                tariff = tariffId,
                autoRenew = 1
            )


            val dataJsonString = json.encodeToString(subscriptionData)


            val request = mapOf(
                "token" to token,
                "u_hash" to uHash,
                "data" to dataJsonString
            )


            val response = api.createSubscription(request)


            if (response.code == "200") {
                println("SubscriptionRepo: Subscription created: ${response.data.subsId}")
                Result.success(response.data.subsId)
            } else {
                Result.failure(Exception("Failed to create subscription: ${response.code}"))
            }
        } catch (e: Exception) {
            println("SubscriptionRepo: Error creating subscription: $e")
            Result.failure(e)
        }
    }


    override suspend fun createPayment(
        amount: Int,
        subsId: String?,
        appUrl: String?
    ): Result<PaymentResponseData> = withContext(Dispatchers.Default) {
        try {
            val token = settings.getString("token", "")
            val uHash = settings.getString("hash", "")


            if (token.isEmpty() || uHash.isEmpty()) {
                return@withContext Result.failure(Exception("No authentication tokens"))
            }


            val paymentData = PaymentData(
                sum = amount,
                currency = "RUB",
                paymentService = 1,
                subsId = subsId,
                paymentWay = 2
            )


            val dataJsonString = json.encodeToString(paymentData)


            val requestMap = mutableMapOf(
                "token" to token,
                "u_hash" to uHash,
                "data" to dataJsonString
            )


            appUrl?.let {
                println("SubscriptionRepo: Adding appUrl to payment request: $it")
                requestMap["appUrl"] = it
            }


            val response = api.createPayment(requestMap)


            if (response.code == "200") {
                println("SubscriptionRepo: Payment created: ${response.data.pId}")
                Result.success(response.data)
            } else {
                Result.failure(Exception("Failed to create payment: ${response.code}"))
            }
        } catch (e: Exception) {
            println("SubscriptionRepo: Error creating payment: $e")
            Result.failure(e)
        }
    }


    override suspend fun getPaymentStatus(paymentId: String): Result<PaymentInfo> = withContext(Dispatchers.Default) {
        try {
            val token = settings.getString("token", "")
            val uHash = settings.getString("hash", "")


            val request = mapOf(
                "token" to token,
                "u_hash" to uHash,
                "p_id" to paymentId
            )


            val response = api.getPayment(request)


            if (response.code == "200" && response.data.payment.isNotEmpty()) {
                Result.success(response.data.payment[0])
            } else {
                Result.failure(Exception("Payment not found"))
            }
        } catch (e: Exception) {
            println("SubscriptionRepo: Error getting payment status: $e")
            Result.failure(e)
        }
    }


    override suspend fun getUserSubscriptions(): Result<List<SubscriptionInfo>> = withContext(Dispatchers.Default) {
        try {
            val token = settings.getString("token", "")
            val uHash = settings.getString("hash", "")
            val uId = settings.getString("userId", "")


            // Сначала пробуем получить из кэша
            val cachedSubscriptions = getCachedSubscriptions(uId)
            
            
            // Делаем запрос к API
            val request = mapOf(
                "token" to token,
                "u_hash" to uHash
            )


            val response = api.getSubscription(request)


            if (response.code == "200") {
                val subscriptions = response.data.subscription ?: emptyList()
                
                
                // Сохраняем в кэш
                cacheSubscriptions(uId, subscriptions)
                
                
                Result.success(subscriptions)
            } else {
                // Если API не работает, возвращаем кэшированные данные
                if (cachedSubscriptions.isNotEmpty()) {
                    println("SubscriptionRepo: Using cached subscriptions")
                    Result.success(cachedSubscriptions)
                } else {
                    Result.failure(Exception("Failed to get subscriptions: ${response.code}"))
                }
            }


        } catch (e: Exception) {
            println("SubscriptionRepo: Error getting subscriptions: $e")
            
            
            // В случае ошибки возвращаем кэшированные данные
            val uId = settings.getString("userId", "")
            val cachedSubscriptions = getCachedSubscriptions(uId)
            
            
            if (cachedSubscriptions.isNotEmpty()) {
                println("SubscriptionRepo: Using cached subscriptions after error")
                Result.success(cachedSubscriptions)
            } else {
                Result.failure(e)
            }
        }
    }


    override suspend fun getTariffs(): Result<Map<String, Map<String, Any>>> = withContext(Dispatchers.Default) {
        try {
            println("SubscriptionRepo: Making getTariffs API call...")
            val response = api.getTariffs()
            println("SubscriptionRepo: getTariffs response code: ${response.code}")


            if (response.code == "200") {
                val tariffs = response.data.data.tariffs ?: emptyMap()
                println("SubscriptionRepo: Tariffs received: ${tariffs.size} items")
                Result.success(tariffs)
            } else {
                println("SubscriptionRepo: Failed to get tariffs: ${response.code}")
                Result.failure(Exception("Failed to get tariffs: ${response.code}"))
            }
        } catch (e: Exception) {
            println("SubscriptionRepo: Error getting tariffs: $e")
            Result.failure(e)
        }
    }
    
    
    /**
     * Получить кэшированные подписки из локальной БД
     */
    private suspend fun getCachedSubscriptions(userId: String): List<SubscriptionInfo> {
        return try {
            val dbSubscriptions = database.subscriptionQueries
                .getSubscriptionsByUserId(userId)
                .asFlow()
                .mapToList(Dispatchers.Default)
                .first()
            
            
            dbSubscriptions.map { dbSub ->
                SubscriptionInfo(
                    subsId = dbSub.subsId,
                    uId = dbSub.uId,
                    tariff = dbSub.tariff,
                    startDate = dbSub.startDate,
                    endDate = dbSub.endDate,
                    cancellationDate = dbSub.cancellationDate,
                    subsStatus = dbSub.subsStatus,
                    autoRenew = dbSub.autoRenew.toInt(),
                    pId = json.decodeFromString(dbSub.pIdJson),
                    paid = dbSub.paid != 0L
                )
            }
        } catch (e: Exception) {
            println("SubscriptionRepo: Error getting cached subscriptions: $e")
            emptyList()
        }
    }
    
    
    /**
     * Сохранить подписки в локальную БД
     */
    private suspend fun cacheSubscriptions(userId: String, subscriptions: List<SubscriptionInfo>) {
        try {
            val currentTime = System.currentTimeMillis()
            
            
            subscriptions.forEach { sub ->
                database.subscriptionQueries.insertOrUpdateSubscription(
                    subsId = sub.subsId,
                    uId = sub.uId,
                    tariff = sub.tariff,
                    startDate = sub.startDate,
                    endDate = sub.endDate,
                    cancellationDate = sub.cancellationDate,
                    subsStatus = sub.subsStatus,
                    autoRenew = sub.autoRenew.toLong(),
                    paid = if (sub.paid) 1L else 0L,
                    pIdJson = json.encodeToString(sub.pId),
                    createdAt = currentTime,
                    updatedAt = currentTime
                )
            }
            
            
            println("SubscriptionRepo: Cached ${subscriptions.size} subscriptions")
        } catch (e: Exception) {
            println("SubscriptionRepo: Error caching subscriptions: $e")
        }
    }
    
    
    /**
     * Отменить подписку (обновить статус в БД)
     */
    suspend fun cancelSubscriptionLocal(subsId: String): Result<Unit> = withContext(Dispatchers.Default) {
        try {
            val currentTime = System.currentTimeMillis()
            database.subscriptionQueries.cancelSubscription(
                cancellationDate = currentTime,
                updatedAt = currentTime,
                subsId = subsId
            )
            Result.success(Unit)
        } catch (e: Exception) {
            println("SubscriptionRepo: Error cancelling subscription locally: $e")
            Result.failure(e)
        }
    }


    companion object {
        const val PAYMENT_CREATED = 1
        const val PAYMENT_CANCELED = 3
        const val PAYMENT_SUCCEEDED = 6
    }
}
