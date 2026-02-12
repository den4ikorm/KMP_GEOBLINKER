package org.example.geoblinker.presentation.features.settings/notification


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для настроек уведомлений
 */
class NotificationSettingsViewModel : BaseViewModel<NotificationSettingsState, NotificationSettingsEvent, NotificationSettingsEffect>(
    initialState = NotificationSettingsState()
) {


    override fun handleEvent(event: NotificationSettingsEvent) {
        when (event) {
            is NotificationSettingsEvent.NavigateBack -> sendEffect(NotificationSettingsEffect.NavigateBack)
            is NotificationSettingsEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
