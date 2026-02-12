package org.example.geoblinker.presentation.features.info/icon_chooser




/**
 * Side-effects для выбора иконки
 */
sealed class IconChooserEffect {
    data object NavigateBack : IconChooserEffect()
    data class ShowError(val message: String) : IconChooserEffect()
    data class ShowMessage(val message: String) : IconChooserEffect()
}
