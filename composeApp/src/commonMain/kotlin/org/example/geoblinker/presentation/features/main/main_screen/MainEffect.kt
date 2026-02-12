package org.example.geoblinker.presentation.features.main/main_screen




/**
 * Side-effects для главного экрана
 */
sealed class MainEffect {
    data object NavigateBack : MainEffect()
    data class ShowError(val message: String) : MainEffect()
    data class ShowMessage(val message: String) : MainEffect()
}
