package org.example.geoblinker.data.repositories


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.geoblinker.db.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.example.geoblinker.domain.models.*
import org.example.geoblinker.domain.repositories.Repository


class RepositoryImpl(
    private val db: AppDatabase,
    private val deviceQueries: DeviceQueries,
    private val typeSignalQueries: TypeSignalQueries,
    private val signalQueries: SignalQueries,
    private val newsQueries: NewsQueries
) : Repository {


    override suspend fun insertAllDevices(devices: List<Devices>) = withContext(Dispatchers.Default) {
        db.transaction {
            devices.forEach { device ->
                insertDeviceInternal(device)
                insertAllTypeSignalInternal(device)
            }
        }
    }


    override suspend fun insertDevice(device: Devices) = withContext(Dispatchers.Default) {
        db.transaction {
            insertDeviceInternal(device)
            insertAllTypeSignalInternal(device)
        }
    }


    private fun insertDeviceInternal(device: Devices) {
        deviceQueries.insertDevice(
            imei = device.imei,
            id = device.id,
            name = device.name,
            isConnected = device.isConnected,
            bindingTime = device.bindingTime,
            simei = device.simei,
            registrationPlate = device.registrationPlate,
            modelName = device.modelName,
            powerRate = device.powerRate,
            signalRate = device.signalRate,
            speed = device.speed,
            lat = device.lat,
            lng = device.lng,
            typeStatus = device.typeStatus,
            deviceType = device.deviceType,
            breakdownForecast = device.breakdownForecast,
            maintenanceRecommendations = device.maintenanceRecommendations,
            markerId = device.markerId
        )
    }


    override suspend fun updateDevice(device: Devices) = withContext(Dispatchers.Default) {
        deviceQueries.updateDevice(
            imei = device.imei,
            id = device.id,
            name = device.name,
            isConnected = device.isConnected,
            bindingTime = device.bindingTime,
            simei = device.simei,
            registrationPlate = device.registrationPlate,
            modelName = device.modelName,
            powerRate = device.powerRate,
            signalRate = device.signalRate,
            speed = device.speed,
            lat = device.lat,
            lng = device.lng,
            typeStatus = device.typeStatus,
            deviceType = device.deviceType,
            breakdownForecast = device.breakdownForecast,
            maintenanceRecommendations = device.maintenanceRecommendations,
            markerId = device.markerId
        )
    }


    override suspend fun updateAllDevices(devices: List<Devices>) = withContext(Dispatchers.Default) {
        db.transaction {
            devices.forEach { device ->
                deviceQueries.updateDevice(
                    imei = device.imei,
                    id = device.id,
                    name = device.name,
                    isConnected = device.isConnected,
                    bindingTime = device.bindingTime,
                    simei = device.simei,
                    registrationPlate = device.registrationPlate,
                    modelName = device.modelName,
                    powerRate = device.powerRate,
                    signalRate = device.signalRate,
                    speed = device.speed,
                    lat = device.lat,
                    lng = device.lng,
                    typeStatus = device.typeStatus,
                    deviceType = device.deviceType,
                    breakdownForecast = device.breakdownForecast,
                    maintenanceRecommendations = device.maintenanceRecommendations,
                    markerId = device.markerId
                )
            }
        }
    }


    override suspend fun clearAllDevices() = withContext(Dispatchers.Default) {
        deviceQueries.deleteAllDevices()
    }


    override fun getAllDevices(): Flow<List<Devices>> {
        return deviceQueries.getAllDevices()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }


    private fun insertAllTypeSignalInternal(device: Devices) {
        val signalTypes = if (device.deviceType == "tracker_model4") {
            listOf(
                SignalType.ShakeAlarm, SignalType.InFence, SignalType.LowPower,
                SignalType.OutFence, SignalType.PowerCut, SignalType.AccOff,
                SignalType.LowBat, SignalType.Speeding, SignalType.SpeedingEnd
            )
        } else {
            listOf(
                SignalType.ShakeAlarm, SignalType.InFence, SignalType.LowPower,
                SignalType.OutFence, SignalType.PowerCut, SignalType.Speeding,
                SignalType.SpeedingEnd
            )
        }


        signalTypes.forEach { type ->
            typeSignalQueries.insertTypeSignal(
                deviceId = device.id,
                type = "DefaultList",
                checked = 1,
                checkedPush = 1,
                checkedEmail = 0,
                checkedAlarm = 0,
                soundUri = null
            )
        }
    }


    override suspend fun getTypeSignal(imei: String): List<TypeSignals> = withContext(Dispatchers.Default) {
        typeSignalQueries.getTypesSignalsByDevice(imei).executeAsList()
    }


    override suspend fun updateTypeSignal(typeSignal: TypeSignals) = withContext(Dispatchers.Default) {
        typeSignalQueries.updateTypeSignal(
            checked = typeSignal.checked,
            checkedPush = typeSignal.checkedPush,
            checkedEmail = typeSignal.checkedEmail,
            checkedAlarm = typeSignal.checkedAlarm,
            soundUri = typeSignal.soundUri,
            id = typeSignal.id,
            deviceId = typeSignal.deviceId,
            type = typeSignal.type
        )
    }


    override suspend fun insertSignal(signal: Signals) = withContext(Dispatchers.Default) {
        signalQueries.insertSignal(
            deviceId = signal.deviceId,
            name = signal.name,
            dateTime = signal.dateTime,
            isSeen = signal.isSeen
        )
    }


    override fun getAllSignals(): Flow<List<Signals>> {
        return signalQueries.getAllSignals()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }


    override fun getAllDeviceSignals(imei: String): Flow<List<Signals>> {
        return signalQueries.getAllSignalsByDevice(imei)
            .asFlow()
            .mapToList(Dispatchers.Default)
    }


    override suspend fun updateSignal(signal: Signals) = withContext(Dispatchers.Default) {
        signalQueries.updateSignal(
            deviceId = signal.deviceId,
            name = signal.name,
            dateTime = signal.dateTime,
            isSeen = signal.isSeen,
            id = signal.id
        )
    }


    override suspend fun insertNews(news: News) = withContext(Dispatchers.Default) {
        newsQueries.insertNews(
            description = news.description,
            dateTime = news.dateTime,
            isSeen = news.isSeen
        )
    }


    override fun getAllNews(): Flow<List<News>> {
        return newsQueries.getAllNews()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }


    override suspend fun clearNews() = withContext(Dispatchers.Default) {
        newsQueries.clearNews()
    }


    override suspend fun updateNews(news: News) = withContext(Dispatchers.Default) {
        newsQueries.updateNews(
            description = news.description,
            dateTime = news.dateTime,
            isSeen = news.isSeen,
            id = news.id
        )
    }
}
