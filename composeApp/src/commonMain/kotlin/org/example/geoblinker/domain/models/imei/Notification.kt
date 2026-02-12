package org.example.geoblinker.domain.models.imei


import kotlin.time.Clock
import kotlinx.serialization.Serializable


@Serializable
data class NotificationItem(
    val imei: Long,


    val time: Long,


    val type: String
)


@Serializable


data class NotificationList(
    val message: String,


    val items: List<NotificationItem>
)


@Serializable
data class NotificationParams(
    val endTime: Long = Clock.System.now().epochSeconds,
    val isDesc: Boolean = true,
    val isSummary: Boolean = false,
    val limitSize: Int = 100,
    val familyId: String,
    val simei: List<String>,
    val startTime: Int = 30,
    val type: List<String>
