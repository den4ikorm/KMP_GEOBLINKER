package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.PaymentInfoResponse
import org.example.geoblinker.domain.models.PaymentResponse
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для управления платежами
 */
class ManagePaymentUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Создание нового платежа
     * @param paymentData - данные платежа
     * @return Result с ответом о создании платежа
     */
    suspend fun createPayment(paymentData: Map<String, String>): Result<PaymentResponse> {
        return try {
            val response = apiRepository.createPayment(paymentData)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    
    /**
     * Получение информации о платеже
     * @param paymentData - данные для запроса
     * @return Result с информацией о платеже
     */
    suspend fun getPaymentInfo(paymentData: Map<String, String>): Result<PaymentInfoResponse> {
        return try {
            val response = apiRepository.getPayment(paymentData)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
