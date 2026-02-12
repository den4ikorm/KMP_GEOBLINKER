package org.example.geoblinker.presentation.features.other/chats




/**
 * State для чатов
 */
data class ChatsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
