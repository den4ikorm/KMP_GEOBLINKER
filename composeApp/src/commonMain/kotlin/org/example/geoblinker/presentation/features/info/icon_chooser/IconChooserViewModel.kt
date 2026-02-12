package org.example.geoblinker.presentation.features.info/icon_chooser


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для выбора иконки
 */
class IconChooserViewModel : BaseViewModel<IconChooserState, IconChooserEvent, IconChooserEffect>(
    initialState = IconChooserState()
) {


    override fun handleEvent(event: IconChooserEvent) {
        when (event) {
            is IconChooserEvent.NavigateBack -> sendEffect(IconChooserEffect.NavigateBack)
            is IconChooserEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
