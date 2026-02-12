package org.example.geoblinker.domain.models


import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject


@Serializable
data class SubscriptionRequest(
    val token: String,
    val uHash: String,
    val data: String
)


@Serializable
data class SubscriptionData(
    val tariff: String,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val autoRenew: Int = 0
)


@Serializable
data class SubscriptionResponse(
    val code: String,
    val data: SubscriptionResponseData
)


@Serializable
data class SubscriptionResponseData(
    val subsId: String
)


@Serializable
data class SubscriptionInfo(
    val subsId: String,
    val uId: String,
    val tariff: String,
    val startDate: Long,
    val endDate: Long,
    val cancellationDate: Long?,
    val subsStatus: String,
    val autoRenew: Int,
    val pId: List<String>,
    val paid: Boolean
)


@Serializable
data class SubscriptionListResponse(
    val code: String,
    val data: SubscriptionListData
)


@Serializable
data class SubscriptionListData(
    val subscription: List<SubscriptionInfo>?
)


@Serializable
data class PaymentRequest(
    val token: String,
    val uHash: String,
    val data: String, // JSON string
    val appUrl: String? = null
)


@Serializable
data class PaymentData(
    val sum: Int,
    val currency: String,
    val paymentService: Int,
    val subsId: String? = null,
    val paymentWay: Int
)


@Serializable
data class PaymentResponse(
    val code: String,
    val data: PaymentResponseData
)


@Serializable
data class PaymentResponseData(
    val pId: String,
    val confirmationUrl: String
)


@Serializable
data class PaymentInfo(
    val sum: String,
    val percent: String,
    val totalSum: String,
    val currency: String,
    val paymentStatus: Int,
    val paymentService: Int,
    val from: String,
    val to: String,
    val timestampCreated: Long,
    val subsId: String?,
    val pIdOuter: String?
)


@Serializable
data class PaymentInfoResponse(
    val code: String,
    val data: PaymentInfoData
)


@Serializable
data class PaymentInfoData(
    val payment: List<PaymentInfo>
)


@Serializable
data class TariffResponse(
    val code: String,
    val data: TariffData
)


@Serializable
data class TariffResponseMap(
    val code: String,
    val data: TariffDataMap
)


@Serializable
data class TariffIndividualData(
    val number:Int,
    val name :String,
    val period:Int
){
    fun getLenInSeconds() = 0
}


@Serializable
data class DataLangResponse(
    val code: String,
    val data: ServerLangData
)




@Serializable
data class DataResponse(
    val code: String,
    val data: ServerCommonData
)




@Serializable
data class TariffData(
    val data: TariffInfo
)


@Serializable
data class TariffDataMap(
    val data: TariffInfoMap
)




@Serializable
data class ServerLangData(
    val data: LangInfo
)




@Serializable
data class ServerCommonData(
    val data: SiteConstants
)


@Serializable
data class SiteConstants(
    val constants: TrackerModes
)


@Serializable
data class TrackerModes(
    val tracker4: TrackerValue,
    val tracker2: TrackerValue
)


@Serializable
data class TrackerValue(
    val jsonString: String
){
    fun getList(): EventTypes =gson.fromJson(jsonString,EventTypes::class)
}


@Serializable
class EventTypes() {
    var list: ArrayList<String>? = null
}




@Serializable
data class TariffInfo(
    val tariffs: Map<String, TariffItem>?,
)


@Serializable
data class TariffInfoMap(
    val tariffs: Map<String, Map<String, JsonObject>>?
)


@Serializable
data class LangInfo(
    val langValues: Map<String, Map<Int,String>>
)




@Serializable
data class TariffItem(
    val name: String,
    val price: Double,
    val period: Int
)


@Serializable
data class LanguageData(
    val langValues: Map<String, Map<Int,String>>
)




