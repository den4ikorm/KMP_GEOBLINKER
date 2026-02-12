package org.example.geoblinker.domain.models.imei


import kotlin.time.Clock
import kotlinx.serialization.Serializable


@Serializable
data class TrajectoryImeiItem(
    val lat: Double,
    val lng: Double,
    val speed: Double,
    val time: Long
)


@Serializable
data class TrajectoryImei(
    val code: Int,
    val message: String,
    val data: List<TrajectoryImeiItem>
)


@Serializable
data class ParamsTrajectoryImei(
    val limitSize: Int = 2000,
    val simei: String,
    val timeBegin: Long = 1,
    val timeEnd: Long = Clock.System.now().epochSeconds
