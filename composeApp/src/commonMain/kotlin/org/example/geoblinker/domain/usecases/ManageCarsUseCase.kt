package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.Cars
import org.example.geoblinker.domain.models.Code
import org.example.geoblinker.domain.models.ResponseCreateCar
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для управления автомобилями пользователя
 */
class ManageCarsUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Получение всех автомобилей пользователя
     * @param authData - данные авторизации
     * @return Result со списком автомобилей
     */
    suspend fun getAllCars(authData: Map<String, String>): Result<Cars> {
        return try {
            val response = apiRepository.getAllCar(authData)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    
    /**
     * Добавление нового автомобиля
     * @param carData - данные автомобиля
     * @return Result с ответом о создании
     */
    suspend fun addCar(carData: Map<String, String>): Result<ResponseCreateCar> {
        return try {
            val response = apiRepository.addCar(carData)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    
    /**
     * Обновление данных автомобиля
     * @param carId - ID автомобиля
     * @param carData - новые данные
     * @return Result с кодом ответа
     */
    suspend fun updateCar(carId: String, carData: Map<String, String>): Result<Code> {
        return try {
            val response = apiRepository.updateCar(carId, carData)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
