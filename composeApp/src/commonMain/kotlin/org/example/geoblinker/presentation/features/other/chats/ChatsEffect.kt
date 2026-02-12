package org.example.geoblinker.presentation.features.other/chats




/**
 * Side-effects для чатов
 */
sealed class ChatsEffect {
    data object NavigateBack : ChatsEffect()
    data class ShowError(val message: String) : ChatsEffect()
    data class ShowMessage(val message: String) : ChatsEffect()
}
