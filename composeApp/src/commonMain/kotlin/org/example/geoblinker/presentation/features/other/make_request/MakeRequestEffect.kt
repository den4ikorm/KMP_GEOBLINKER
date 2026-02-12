package org.example.geoblinker.presentation.features.other/make_request




/**
 * Side-effects для создания заявки
 */
sealed class MakeRequestEffect {
    data object NavigateBack : MakeRequestEffect()
    data class ShowError(val message: String) : MakeRequestEffect()
    data class ShowMessage(val message: String) : MakeRequestEffect()
}
