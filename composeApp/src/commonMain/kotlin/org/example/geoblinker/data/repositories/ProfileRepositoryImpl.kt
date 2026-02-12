package org.example.geoblinker.data.repositories


import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.datetime.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.geoblinker.domain.models.Profile
import org.example.geoblinker.domain.models.ProfileData
import org.example.geoblinker.domain.models.WayConfirmationCode
import org.example.geoblinker.domain.repositories.ApiRepository
import org.example.geoblinker.domain.repositories.ProfileRepository


private val INITIAL_WAYS = listOf(
    WayConfirmationCode("Telegram"),
    WayConfirmationCode("WhatsApp"),
    WayConfirmationCode("SMS"),
    WayConfirmationCode("Email")
)


class ProfileRepositoryImpl(
    private val api: ApiRepository,
    private val settings: Settings,
    private val json: Json
) : ProfileRepository {


    override suspend fun getProfileData(): ProfileData {
        val name = settings.getString("name", "Константин Гусевский")
        val phone = settings.getString("phone", "")
        val email = settings.getString("email", "")
        val isLogin = settings.getBoolean("login", false)


        val subDate = calculateSubscriptionDate()


        var orderWays = settings.getString("orderWays", "0123")
        if (orderWays.length < INITIAL_WAYS.size) orderWays = "0123"


        val ways = List(INITIAL_WAYS.size) { index ->
            val orderIndex = try {
                orderWays[index].digitToInt()
            } catch (e: Exception) { index }


            val defaultWay = INITIAL_WAYS[orderIndex]
            val isChecked = settings.getBoolean(defaultWay.text, false)
            WayConfirmationCode(defaultWay.text, isChecked)
        }


        return ProfileData(name, phone, email, isLogin, subDate, orderWays, ways)
    }


    override suspend fun refreshSubscriptionDate(): Long {
        return calculateSubscriptionDate()
    }


    private fun calculateSubscriptionDate(): Long {
        val maxEndDate = settings.getLong("max_subscription_end_date", 0)
        val oldSubscription = settings.getLong("subscription", 0)


        return if (maxEndDate > 0) {
            maxEndDate * 1000
        } else {
            oldSubscription
        }
    }


    override suspend fun addSubscriptionMonths(currentDate: Long, months: Int): Long {
        val instant = Instant.fromEpochMilliseconds(currentDate)
        val newTime = instant.plus(months, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())
            .toEpochMilliseconds()


        settings.putLong("subscription", newTime)
        return newTime
    }


    override suspend fun updateName(name: String): Result<Unit> {
        return try {
            val token = settings.getString("token", "")
            val hash = settings.getString("hash", "")


            val request = mapOf(
                "token" to token,
                "u_hash" to hash,
                "data" to json.encodeToString(Profile(name = name))
            )


            val res = api.editUser(request)
            if (res.code == "200") {
                settings.putString("name", name)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Server error: ${res.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun updateEmail(email: String): Result<Unit> {
        return try {
            val token = settings.getString("token", "")
            val hash = settings.getString("hash", "")


            val request = mapOf(
                "token" to token,
                "u_hash" to hash,
                "data" to json.encodeToString(Profile(email = email))
            )


            val res = api.editUser(request)
            if (res.code == "200") {
                settings.putString("email", email)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Server error: ${res.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun setPhone(phone: String) {
        settings.putString("phone", phone)
    }


    override suspend fun saveWaysOrder(orderString: String) {
        settings.putString("orderWays", orderString)
    }


    override suspend fun setWayChecked(wayName: String, isChecked: Boolean) {
        settings.putBoolean(wayName, isChecked)
    }


    override suspend fun logout() {
        val keysToRemove = listOf(
            "selected_tariff_id", "max_subscription_end_date", "current_payment_id",
            "trackerNamesMap", "tracker2EventList", "tracker4EventList",
            "token", "hash", "login", "success_message", "error_message",
            "unitsDistance", "updateMap", "name", "phone", "subscription",
            "lastTime", "sid", "sidFamily", "email", "orderWays"
        )
        keysToRemove.forEach { settings.remove(it) }
        INITIAL_WAYS.forEach { settings.remove(it.text) }
    }
}
