package org.example.geoblinker.domain.models


import kotlinx.serialization.Serializable


@Serializable
public data class ChatTechSupport(
  public val id: Long,
  public val title: String,
  public val lastMessageTime: Long,
  public val lastChecked: Long,
  public val decided: Long,
