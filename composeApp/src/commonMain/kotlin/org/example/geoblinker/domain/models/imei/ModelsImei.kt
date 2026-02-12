package org.example.geoblinker.domain.models.imei


import kotlinx.serialization.Serializable


@Serializable
data class RequestImei(
    val module: String,
    val func: String,
    val params: ParamsImei
