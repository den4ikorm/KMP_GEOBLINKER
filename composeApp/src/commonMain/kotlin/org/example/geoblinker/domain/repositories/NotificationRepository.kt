package org.example.geoblinker.domain.repositories


import app.cash.sqldelight.db.QueryResult
import org.example.geoblinker.domain.models.*
import org.example.geoblinker.domain.models.imei.NotificationList


interface NotificationRepository {


    suspend fun getListNotifications(sid: String, sidFamily: String): NotificationList
    suspend fun getDevice(imei: String): Devices?
    suspend fun getTypesSignal(id: String, type: String): TypeSignals?
    suspend fun insertSignal(signal: Signals): QueryResult<Long>


