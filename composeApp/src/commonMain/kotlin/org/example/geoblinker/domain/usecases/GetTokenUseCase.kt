package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.Token
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для получения токена
 */
class GetTokenUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Получение токена по хэшу авторизации
     * @param authHash - хэш авторизации
     * @return Result с токеном
     */
    suspend operator fun invoke(authHash: String): Result<Token> {
        return try {
            val response = apiRepository.getToken(authHash)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
