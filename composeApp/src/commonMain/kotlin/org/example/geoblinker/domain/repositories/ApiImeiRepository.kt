package org.example.geoblinker.domain.repositories


import org.example.geoblinker.domain.models.imei.*


interface ApiImeiRepository {


    suspend fun login(request: RequestImei): LoginImei
    suspend fun getDeviceList(sid: String, request: RequestImei): GetDeviceListImei
    suspend fun add( sid: String, request: RequestImei): AddImei
    suspend fun getDetail( sid: String, request: RequestImei ): GetDetailImei
    suspend fun getSignalList( sid: String, request: RequestImei): NotificationList
    suspend fun getTrajectory( sid: String, request: RequestImei ): TrajectoryImei


