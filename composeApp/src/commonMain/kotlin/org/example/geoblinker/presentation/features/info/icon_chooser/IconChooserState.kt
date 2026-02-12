package org.example.geoblinker.presentation.features.info/icon_chooser




/**
 * State для выбора иконки
 */
data class IconChooserState(
    val isLoading: Boolean = false,
    val error: String? = null
)
