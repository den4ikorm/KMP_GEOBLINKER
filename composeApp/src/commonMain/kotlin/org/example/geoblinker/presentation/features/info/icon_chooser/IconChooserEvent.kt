package org.example.geoblinker.presentation.features.info/icon_chooser




/**
 * События для выбора иконки
 */
sealed class IconChooserEvent {
    data object NavigateBack : IconChooserEvent()
    data object ClearError : IconChooserEvent()
}
