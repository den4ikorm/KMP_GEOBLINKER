package org.example.geoblinker.domain.repositories


import org.example.geoblinker.domain.models.ProfileData


interface ProfileRepository {
    suspend fun getProfileData(): ProfileData
    suspend fun refreshSubscriptionDate(): Long
    suspend fun addSubscriptionMonths(currentDate: Long, months: Int): Long


    suspend fun updateName(name: String): Result<Unit>
    suspend fun updateEmail(email: String): Result<Unit>
    suspend fun setPhone(phone: String)


    suspend fun saveWaysOrder(orderString: String)
    suspend fun setWayChecked(wayName: String, isChecked: Boolean)


    suspend fun logout()
