package org.example.geoblinker.presentation.features.subscription/subscription_ready


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для завершения подписки
 */
class SubscriptionReadyViewModel : BaseViewModel<SubscriptionReadyState, SubscriptionReadyEvent, SubscriptionReadyEffect>(
    initialState = SubscriptionReadyState()
) {


    override fun handleEvent(event: SubscriptionReadyEvent) {
        when (event) {
            is SubscriptionReadyEvent.NavigateBack -> sendEffect(SubscriptionReadyEffect.NavigateBack)
            is SubscriptionReadyEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
