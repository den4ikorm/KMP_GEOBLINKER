package org.example.geoblinker.domain.repositories


import app.cash.sqldelight.db.QueryResult
import kotlinx.coroutines.flow.Flow
import org.example.geoblinker.domain.models.*


interface Repository {


    suspend fun insertAllDevices(devices: List<Devices>)


    suspend fun insertDevice(device: Devices)


    suspend fun updateDevice(device: Devices): QueryResult<Long>


    suspend fun updateAllDevices(devices: List<Devices>)


    suspend fun clearAllDevices(): QueryResult<Long>


    fun getAllDevices(): Flow<List<Devices>>


    suspend fun getTypeSignal(imei: String): List<TypeSignals>


    suspend fun updateTypeSignal(typeSignal: TypeSignals): QueryResult<Long>


    suspend fun insertSignal(signal: Signals): QueryResult<Long>


    fun getAllSignals(): Flow<List<Signals>>


    fun getAllDeviceSignals(imei: String): Flow<List<Signals>>


    suspend fun updateSignal(signal: Signals): QueryResult<Long>


    suspend fun insertNews(news: News): QueryResult<Long>


    fun getAllNews(): Flow<List<News>>


    suspend fun clearNews(): QueryResult<Long>


    suspend fun updateNews(news: News): QueryResult<Long>


