package org.example.geoblinker.presentation.features.other/chats




/**
 * События для чатов
 */
sealed class ChatsEvent {
    data object NavigateBack : ChatsEvent()
    data object ClearError : ChatsEvent()
}
