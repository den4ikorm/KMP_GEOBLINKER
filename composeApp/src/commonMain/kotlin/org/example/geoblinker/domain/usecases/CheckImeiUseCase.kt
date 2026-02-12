package org.example.geoblinker.domain.usecases


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.geoblinker.core.session.SessionManager
import org.example.geoblinker.domain.models.imei.AddParamsImei
import org.example.geoblinker.domain.models.imei.RequestImei
import org.example.geoblinker.domain.repositories.ApiImeiRepository
import org.example.geoblinker.domain.repositories.Repository


/**
 * UseCase для проверки и добавления IMEI
 * Из LEGACY_RESOURCES.md:115-149 (DeviceViewModel.kt:224-260)
 */
class CheckImeiUseCase(
    private val apiImeiRepository: ApiImeiRepository,
    private val repository: Repository,
    private val sessionManager: SessionManager
) {
    
    
    suspend fun execute(imei: String): Result<String> = withContext(Dispatchers.Default) {
        try {
            // 1. Проверка на дубликат в локальной БД
            val existingDevice = repository.getDeviceByImei(imei)
            if (existingDevice != null && existingDevice.isConnected == 1L) {
                return@withContext Result.failure(
                    Exception("IMEI уже привязан к устройству: ${existingDevice.name}")
                )
            }
            
            
            // 2. Добавление устройства через IMEI API
            val sid = sessionManager.getSid()
            val sidFamily = sessionManager.getSidFamily()
            
            
            if (sid.isEmpty() || sidFamily.isEmpty()) {
                return@withContext Result.failure(
                    Exception("Сессия IMEI не инициализирована. Выполните синхронизацию.")
                )
            }
            
            
            val response = apiImeiRepository.add(
                sid,
                RequestImei(
                    module = "device",
                    func = "Add",
                    params = AddParamsImei(
                        info = listOf(mapOf("imei" to imei.toLong())),
                        sgid = sidFamily
                    )
                )
            )
            
            
            if (response.items.isEmpty()) {
                return@withContext Result.failure(
                    Exception("IMEI не найден в системе")
                )
            }
            
            
            // 3. Получение simei для нового устройства
            val newSimei = response.items[0].simei
            
            
            Result.success(newSimei)
            
            
        } catch (e: NumberFormatException) {
            Result.failure(Exception("IMEI должен содержать только цифры"))
        } catch (e: Exception) {
            println("CheckImeiUseCase error: ${e.message}")
            Result.failure(e)
        }
    }
}
