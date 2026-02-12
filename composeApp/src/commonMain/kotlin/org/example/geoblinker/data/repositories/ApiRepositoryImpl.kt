package org.example.geoblinker.data.repositories


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import org.example.geoblinker.domain.models.*
import org.example.geoblinker.domain.repositories.ApiRepository


class ApiRepositoryImpl(
    private val client: HttpClient
) : ApiRepository {


    private val baseUrl = "https://ibronevik.ru/taxi/c/0/api/v1/"
    private fun apiUrl(path: String) = "$baseUrl$path"


    private fun Map<String, String>.toFormData(): FormDataContent {
        return FormDataContent(Parameters.build {
            this@toFormData.forEach { (key, value) ->
                append(key, value)
            }
        })
    }


    override suspend fun register(request: Map<String, String>): Code {
        return client.post(apiUrl("register")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun auth(request: Map<String, String>): Authorization {
        return client.post(apiUrl("auth")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun getToken(hash: String): Token {
        return client.post(apiUrl("token/authorized")) {
            setBody(FormDataContent(Parameters.build {
                append("auth_hash", hash)
            }))
        }.body()
    }


    override suspend fun getHash(token: String): Token {
        return client.get(apiUrl("token")) {
            parameter("token", token)
        }.body()
    }


    override suspend fun editUserData(userName: String, request: Map<String, String>): Code {
        return client.post(apiUrl("user/$userName")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun editUser(request: Map<String, String>): Code {
        return client.post(apiUrl("user")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun addCar(request: Map<String, String>): ResponseCreateCar {
        return client.post(apiUrl("car")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun getAllCar(request: Map<String, String>): Cars {
        return client.post(apiUrl("user/authorized/car")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun updateCar(cId: String, request: Map<String, String>): Code {
        return client.post(apiUrl("car/$cId")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun sendEmailTechSupport(request: Map<String, String>): Code {
        return client.post(apiUrl("mail/1/send/")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun createSubscription(request: Map<String, String>): SubscriptionResponse {
        return client.post(apiUrl("subscription/create")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun getSubscription(request: Map<String, String>): SubscriptionListResponse {
        return client.post(apiUrl("subscription/get")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun createPayment(request: Map<String, String>): PaymentResponse {
        return client.post(apiUrl("payment/create")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun getPayment(request: Map<String, String>): PaymentInfoResponse {
        return client.post(apiUrl("payment/get")) {
            setBody(request.toFormData())
        }.body()
    }


    override suspend fun getTariffs(): TariffResponseMap {
        return client.get(apiUrl("data")).body()
    }


    override suspend fun getLangData(langId: String): DataLangResponse {
        return client.get(apiUrl("data")) {
            parameter("data.lang_vls", langId)
        }.body()
    }


    override suspend fun getDeviceSignalsData(s: String): DataResponse {
        return client.get(apiUrl("data")) {
            parameter("data.site_constants", s)
        }.body()
    }
