package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.Code
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для обновления профиля пользователя
 */
class UpdateUserProfileUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Обновление данных пользователя
     * @param userName - имя пользователя
     * @param data - данные для обновления
     * @return Result с кодом ответа
     */
    suspend operator fun invoke(userName: String, data: Map<String, String>): Result<Code> {
        return try {
            val response = apiRepository.editUserData(userName, data)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    
    /**
     * Обновление данных текущего пользователя
     * @param data - данные для обновления
     * @return Result с кодом ответа
     */
    suspend fun updateCurrent(data: Map<String, String>): Result<Code> {
        return try {
            val response = apiRepository.editUser(data)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
