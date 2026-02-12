package org.example.geoblinker.domain.models


import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable


@Serializable
public data class MessageTechSupport(
  public val id: Long,
  public val chatId: Long,
  public val content: String,
  public val timeStamp: Long,
  public val isMy: Long,
  public val typeMessage: String,
  public val photoUri: String?,
)
