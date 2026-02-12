package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.Code
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для регистрации пользователя
 */
class RegisterUserUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Регистрация пользователя по номеру телефона
     * @param phone - номер телефона
     * @return Result с кодом ответа
     */
    suspend operator fun invoke(phone: String): Result<Code> {
        return try {
            val request = mapOf("phone" to phone)
            val response = apiRepository.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
