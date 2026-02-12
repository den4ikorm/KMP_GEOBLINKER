package org.example.geoblinker.domain.models




data class ProfileData(
    val name: String,
    val phone: String,
    val email: String,
    val isLogin: Boolean,
    val subscriptionEnd: Long,
    val orderWaysString: String,
    val ways: List<WayConfirmationCode>
