package org.example.geoblinker.domain.models


import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable


@Serializable
public data class Signals(
  public val id: Long,
  public val deviceId: String,
  public val name: String,
  public val dateTime: Long,
  public val isSeen: Long,
)
