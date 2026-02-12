package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.TariffResponseMap
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для получения тарифов
 */
class GetTariffsUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Получение списка доступных тарифов
     * @return Result с тарифами
     */
    suspend operator fun invoke(): Result<TariffResponseMap> {
        return try {
            val response = apiRepository.getTariffs()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
