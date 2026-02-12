package org.example.geoblinker.domain.models


import kotlinx.serialization.Serializable


@Serializable
data class Authorization(
    val code: String,
    val user: User?,
    val hash: String?
)


@Serializable
data class User(
    val name: String,
    val email: String?,
    val photo: String
)


@Serializable
data class Token(
    val data: DataToken
)


@Serializable
data class DataToken(
    val token: String,
    val hash: String
