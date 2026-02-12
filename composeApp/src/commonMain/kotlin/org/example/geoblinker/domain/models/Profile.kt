package org.example.geoblinker.domain.models


import kotlinx.serialization.Serializable


@Serializable
data class Profile(
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val photo: String? = null
)
