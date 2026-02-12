package org.example.geoblinker.data.repositories


import com.example.geoblinker.db.DeviceQueries
import com.example.geoblinker.db.SignalQueries
import com.example.geoblinker.db.TypeSignalQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.geoblinker.domain.models.Devices
import org.example.geoblinker.domain.models.Signals
import org.example.geoblinker.domain.models.TypeSignals
import org.example.geoblinker.domain.models.imei.NotificationList
import org.example.geoblinker.domain.repositories.ApiImeiRepository
import org.example.geoblinker.domain.repositories.NotificationRepository


class NotificationRepositoryImpl(
    private val apiService: ApiImeiRepository,
    private val deviceQueries: DeviceQueries,
    private val typeSignalQueries: TypeSignalQueries,
    private val signalQueries: SignalQueries
) : NotificationRepository {


    override suspend fun getListNotifications(sid: String, sidFamily: String): NotificationList = withContext(Dispatchers.Default) {
        val simei = deviceQueries.getAllConnectedSimei().executeAsList()
        val res = apiService.getSignalList(sid, sidFamily, simei)


        return@withContext res
    }


    override suspend fun getDevice(imei: String): Devices? = withContext(Dispatchers.Default) {
        deviceQueries.getDeviceByImei(imei)
            .executeAsOneOrNull()
    }


    override suspend fun getTypesSignal(id: String, type: String): TypeSignals? = withContext(Dispatchers.Default) {
        typeSignalQueries.getTypeSignal(id, type)
            .executeAsOneOrNull()
    }


    override suspend fun insertSignal(signal: Signals) = withContext(Dispatchers.Default) {
        signalQueries.insertSignal(
            deviceId = signal.deviceId,
            name = signal.name,
            dateTime = signal.dateTime,
            isSeen = signal.isSeen
        )
    }
