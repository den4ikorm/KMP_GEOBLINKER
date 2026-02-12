package org.example.geoblinker.domain.models


import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject


@Serializable
data class Code(
    val code: String,
    val message: String? = null,
    val data: JsonObject? = null
