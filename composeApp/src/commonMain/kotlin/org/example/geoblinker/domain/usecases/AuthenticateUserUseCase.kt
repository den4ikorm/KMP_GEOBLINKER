package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.Authorization
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для аутентификации пользователя
 */
class AuthenticateUserUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Аутентификация пользователя с кодом подтверждения
     * @param phone - номер телефона
     * @param code - код подтверждения
     * @return Result с данными авторизации
     */
    suspend operator fun invoke(phone: String, code: String): Result<Authorization> {
        return try {
            val request = mapOf(
                "phone" to phone,
                "code" to code
            )
            val response = apiRepository.auth(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
