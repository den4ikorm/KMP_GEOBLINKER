package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.SubscriptionListResponse
import org.example.geoblinker.domain.models.SubscriptionResponse
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для управления подписками
 */
class ManageSubscriptionUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Создание новой подписки
     * @param subscriptionData - данные подписки
     * @return Result с ответом о создании подписки
     */
    suspend fun createSubscription(subscriptionData: Map<String, String>): Result<SubscriptionResponse> {
        return try {
            val response = apiRepository.createSubscription(subscriptionData)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    
    /**
     * Получение списка подписок
     * @param authData - данные авторизации
     * @return Result со списком подписок
     */
    suspend fun getSubscriptions(authData: Map<String, String>): Result<SubscriptionListResponse> {
        return try {
            val response = apiRepository.getSubscription(authData)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
