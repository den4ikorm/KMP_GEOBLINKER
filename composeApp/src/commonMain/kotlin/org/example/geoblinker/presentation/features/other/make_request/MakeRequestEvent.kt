package org.example.geoblinker.presentation.features.other/make_request




/**
 * События для создания заявки
 */
sealed class MakeRequestEvent {
    data object NavigateBack : MakeRequestEvent()
    data object ClearError : MakeRequestEvent()
}
