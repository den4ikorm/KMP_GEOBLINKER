package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.DataResponse
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для получения данных сигналов устройств
 */
class GetDeviceSignalsUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Получение данных сигналов устройства
     * @param deviceId - идентификатор устройства (опционально)
     * @return Result с данными сигналов
     */
    suspend operator fun invoke(deviceId: String = ""): Result<DataResponse> {
        return try {
            val response = apiRepository.getDeviceSignalsData(deviceId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
