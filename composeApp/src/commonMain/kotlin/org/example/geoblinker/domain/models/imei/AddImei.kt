package org.example.geoblinker.domain.models.imei


import kotlinx.serialization.Serializable


@Serializable
data class AddImei(
    val items: List<Imei>
)


@Serializable


data class AddParamsImei(
    val info: List<Map<String, Long>>,
    val sgid: String
):ParamsImei


@Serializable


data class Imei(
    val simei: String
