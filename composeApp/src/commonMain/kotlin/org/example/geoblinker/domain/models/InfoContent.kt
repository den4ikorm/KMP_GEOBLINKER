package org.example.geoblinker.domain.models


import kotlinx.serialization.Serializable


@Serializable
data class InfoContent(
    val title: String = "",
    val body: String = ""
)
