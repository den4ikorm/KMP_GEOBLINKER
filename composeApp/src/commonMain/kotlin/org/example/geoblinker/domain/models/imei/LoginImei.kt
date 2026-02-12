package org.example.geoblinker.domain.models.imei


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
private const val LOGIN_IMEI = "georule"
private const val PASSWORD_IMEI = "8bbe1a8ed834b27261f2a4dfb1418ae7"
@Serializable
data class LoginImei(
    val sid: String,
    @SerialName("familys")
    val family: List<Map<String, JsonElement>>
):ParamsImei
@Serializable
data class LoginParamsImei(
    val account: String = LOGIN_IMEI,
    val platform: String = "web",
    val password: String = PASSWORD_IMEI,


    val type: Int = 58
