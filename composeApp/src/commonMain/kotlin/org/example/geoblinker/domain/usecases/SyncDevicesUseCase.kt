package org.example.geoblinker.domain.usecases


import kotlin.math.*
import org.example.geoblinker.core.NetworkResult
import org.example.geoblinker.domain.models.DeviceSource
import org.example.geoblinker.domain.models.Devices
import org.example.geoblinker.domain.models.SyncedDevice
import org.example.geoblinker.domain.models.imei.GetDetailImei
import org.example.geoblinker.domain.repositories.ApiImeiRepository
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для синхронизации устройств из двух API
 * 
 * Алгоритм 5-шаговой синхронизации:
 * 1. Main API - получить список устройств
 * 2. IMEI API - аутентификация (получить JSESSIONID)
 * 3. IMEI API - получить список трекеров
 * 4. IMEI API - получить детали по каждому IMEI
 * 5. MERGE - объединить данные в SyncedDevice
 */
class SyncDevicesUseCase(
    private val apiRepository: ApiRepository,
    private val apiImeiRepository: ApiImeiRepository
) {
    /**
     * Выполнить синхронизацию устройств
     * 
     * @param token - токен авторизации для Main API
     * @return NetworkResult с списком синхронизированных устройств
     */
    suspend fun execute(token: String): NetworkResult<List<SyncedDevice>> {
        return try {
            println("🔄 [SYNC] Начало синхронизации устройств")
            
            
            // ШАГ 1: Main API - получить устройства
            println("📡 [SYNC] ШАГ 1/5: Получение устройств из Main API...")
            val mainDevices = apiRepository.getDevices(token)
            println("✅ [SYNC] Получено устройств из Main API: ${mainDevices.size}")
            
            
            // ШАГ 2: IMEI API - аутентификация
            println("🔐 [SYNC] ШАГ 2/5: Аутентификация в IMEI API...")
            val jsessionId = apiImeiRepository.login()
            println("✅ [SYNC] JSESSIONID получен")
            
            
            // ШАГ 3: IMEI API - список трекеров
            println("📡 [SYNC] ШАГ 3/5: Получение списка трекеров...")
            val imeiDeviceList = apiImeiRepository.getDeviceList(jsessionId)
            println("✅ [SYNC] Получено трекеров: ${imeiDeviceList.size}")
            
            
            // ШАГ 4: IMEI API - детали по каждому IMEI
            println("📡 [SYNC] ШАГ 4/5: Получение деталей по каждому трекеру...")
            val imeiDetailsMap = mutableMapOf<String, GetDetailImei>()
            
            
            for (device in imeiDeviceList) {
                try {
                    val detail = apiImeiRepository.getDeviceDetail(
                        jsessionId = jsessionId,
                        imei = device.imei
                    )
                    imeiDetailsMap[device.imei] = detail
                    println("  ✓ ${device.imei}")
                } catch (e: Exception) {
                    println("  ⚠ ${device.imei} - ошибка: ${e.message}")
                }
            }
            println("✅ [SYNC] Получено деталей: ${imeiDetailsMap.size}")
            
            
            // ШАГ 5: MERGE - объединение данных
            println("🔀 [SYNC] ШАГ 5/5: Объединение данных...")
            val syncedDevices = mergeDevices(mainDevices, imeiDetailsMap)
            println("✅ [SYNC] Синхронизировано устройств: ${syncedDevices.size}")
            
            
            // Статистика
            val syncedCount = syncedDevices.count { it.isSynced }
            val mainOnlyCount = syncedDevices.count { it.source == DeviceSource.MAIN_API }
            println("📊 [SYNC] Статистика:")
            println("   - Полностью синхронизировано: $syncedCount")
            println("   - Только Main API: $mainOnlyCount")
            println("   - Всего: ${syncedDevices.size}")
            
            
            NetworkResult.Success(syncedDevices)
            
            
        } catch (e: Exception) {
            println("❌ [SYNC] Ошибка синхронизации: ${e.message}")
            e.printStackTrace()
            NetworkResult.Error(
                message = e.message ?: "Ошибка синхронизации устройств",
                exception = e
            )
        }
    }
    
    
    /**
     * Объединить данные из Main API и IMEI API
     */
    private fun mergeDevices(
        mainDevices: List<Devices>,
        imeiDetails: Map<String, GetDetailImei>
    ): List<SyncedDevice> {
        return mainDevices.map { mainDevice ->
            // Поиск соответствующего IMEI устройства
            val imeiDetail = findMatchingImeiDevice(mainDevice, imeiDetails)
            
            
            SyncedDevice(
                // FROM MAIN API
                id = mainDevice.id.toString(),
                name = mainDevice.name ?: "Устройство #${mainDevice.id}",
                latitude = imeiDetail?.lat ?: mainDevice.lat,
                longitude = imeiDetail?.lng ?: mainDevice.lng,
                carId = mainDevice.carId,
                
                
                // FROM IMEI API
                imei = imeiDetail?.imei,
                speed = imeiDetail?.speed?.toDoubleOrNull()?.let { it * 3.6 }, // m/s -> km/h
                altitude = imeiDetail?.altitude?.toIntOrNull(),
                lastUpdate = imeiDetail?.gpsTime,
                gpsQuality = imeiDetail?.gpsSignal?.toIntOrNull(),
                battery = imeiDetail?.electricity?.toIntOrNull(),
                
                
                // MERGED
                markerType = mainDevice.markerId?.toString() ?: "m_0",
                source = when {
                    imeiDetail != null -> DeviceSource.SYNCED
                    else -> DeviceSource.MAIN_API
                },
                isSynced = imeiDetail != null,
                syncError = null
            )
        }
    }
    
    
    /**
     * Найти соответствующее IMEI устройство для Main устройства
     */
    private fun findMatchingImeiDevice(
        mainDevice: Devices,
        imeiDetails: Map<String, GetDetailImei>
    ): GetDetailImei? {
        // Стратегия 1: По IMEI (если есть в Main API)
        mainDevice.imei?.let { imei ->
            imeiDetails[imei]?.let { return it }
        }
        
        
        // Стратегия 2: По названию (если совпадает)
        val mainName = mainDevice.name?.trim()?.lowercase()
        if (mainName != null) {
            imeiDetails.values.find { 
                it.imei.trim().lowercase() == mainName ||
                it.imei.contains(mainName, ignoreCase = true)
            }?.let { return it }
        }
        
        
        // Стратегия 3: По координатам (если близко)
        if (mainDevice.lat != null && mainDevice.lng != null) {
            imeiDetails.values.find { imeiDevice ->
                if (imeiDevice.lat != null && imeiDevice.lng != null) {
                    val distance = calculateDistance(
                        mainDevice.lat, mainDevice.lng,
                        imeiDevice.lat, imeiDevice.lng
                    )
                    distance < 100.0 // менее 100 метров
                } else {
                    false
                }
            }?.let { return it }
        }
        
        
        return null
    }
    
    
    /**
     * Вычислить расстояние между двумя точками (Haversine formula)
     * @return расстояние в метрах
     */
    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371000.0 // Радиус Земли в метрах
        
        
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val deltaLat = Math.toRadians(lat2 - lat1)
        val deltaLon = Math.toRadians(lon2 - lon1)
        
        
        val a = sin(deltaLat / 2).pow(2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(deltaLon / 2).pow(2)
        
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        
        return R * c
    }
}
