package org.example.geoblinker.domain.models


import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable


@Serializable
public data class News(
  public val id: Long,
  public val description: String,
  public val dateTime: Long,
  public val isSeen: Long,
)
