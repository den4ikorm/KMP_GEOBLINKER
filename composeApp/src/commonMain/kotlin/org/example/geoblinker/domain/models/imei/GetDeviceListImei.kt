package org.example.geoblinker.domain.models.imei


import kotlinx.serialization.Serializable


@Serializable
data class GetDeviceListImei(
    val items: List<DeviceImei>
)


@Serializable
data class DeviceImei(
    val sgid: String,
    val imei: Long,
    val simei: String
)


@Serializable
data class GetDeviceListParamsImei(
    val family: String
