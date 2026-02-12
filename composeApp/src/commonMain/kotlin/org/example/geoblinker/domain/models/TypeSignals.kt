package org.example.geoblinker.domain.models


import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable


@Serializable
public data class TypeSignals(
  public val id: Long,
  public val deviceId: String,
  public val type: String,
  public val checked: Long,
  public val checkedPush: Long,
  public val checkedEmail: Long,
  public val checkedAlarm: Long,
  public val soundUri: String?,
)
