package org.example.geoblinker.domain.models.imei


import kotlinx.serialization.Serializable


@Serializable
data class GetDetailImei(
    val posString: String?,
    val powerRate: Int,
    val signalRate: Int,
    val modelName: String,
    val error: String
):ParamsImei


@Serializable
data class PosData(
    val lat: Long,
    val lon: Long,
    val speed: Int
)


@Serializable
data class GetDetailParamsImei(
    val simei: String
