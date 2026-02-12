package org.example.geoblinker.domain.models


import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable


@Serializable
public data class Devices(
  public val imei: String,
  public val id: String,
  public val name: String,
  public val isConnected: Long,
  public val bindingTime: Long,
  public val simei: String,
  public val registrationPlate: String,
  public val modelName: String,
  public val powerRate: Long,
  public val signalRate: Long,
  public val speed: Double,
  public val lat: Double,
  public val lng: Double,
  public val typeStatus: String,
  public val deviceType: String,
  public val breakdownForecast: String?,
  public val maintenanceRecommendations: String?,
  public val markerId: Long,
)
