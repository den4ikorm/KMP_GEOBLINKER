package org.example.geoblinker.data.repositories


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.geoblinker.domain.models.imei.AddImei
import org.example.geoblinker.domain.models.imei.GetDetailImei
import org.example.geoblinker.domain.models.imei.GetDeviceListImei
import org.example.geoblinker.domain.models.imei.LoginImei
import org.example.geoblinker.domain.models.imei.NotificationList
import org.example.geoblinker.domain.models.imei.RequestImei
import org.example.geoblinker.domain.models.imei.TrajectoryImei
import org.example.geoblinker.domain.repositories.ApiImeiRepository


class ApiImeiRepositoryImpl(
    private val client: HttpClient
) : ApiImeiRepository {


    private val baseUrl = "https://www.gps666.net/"


    private fun apiUrl() = "${baseUrl}mapi"


    override suspend fun login(request: RequestImei): LoginImei {
        return client.post(apiUrl()) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }


    override suspend fun getDeviceList(sid: String, request: RequestImei): GetDeviceListImei {
        return client.post(apiUrl()) {
            contentType(ContentType.Application.Json)
            parameter("sid", sid)
            setBody(request)
        }.body()
    }


    override suspend fun add(sid: String, request: RequestImei): AddImei {
        return client.post(apiUrl()) {
            contentType(ContentType.Application.Json)
            parameter("sid", sid)
            setBody(request)
        }.body()
    }


    override suspend fun getDetail(sid: String, request: RequestImei): GetDetailImei {
        return client.post(apiUrl()) {
            contentType(ContentType.Application.Json)
            parameter("sid", sid)
            setBody(request)
        }.body()
    }


    override suspend fun getSignalList(sid: String, request: RequestImei): NotificationList {
        return client.post(apiUrl()) {
            contentType(ContentType.Application.Json)
            parameter("sid", sid)
            setBody(request)
        }.body()
    }


    override suspend fun getTrajectory(sid: String, request: RequestImei): TrajectoryImei {
        return client.post(apiUrl()) {
            contentType(ContentType.Application.Json)
            parameter("sid", sid)
            setBody(request)
        }.body()
    }
