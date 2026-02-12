package org.example.geoblinker.presentation.features.subscription/subscription_two


import kotlinx.coroutines.flow.update
import org.example.geoblinker.core.base.BaseViewModel


/**
 * ViewModel для второго шага подписки
 */
class SubscriptionTwoViewModel : BaseViewModel<SubscriptionTwoState, SubscriptionTwoEvent, SubscriptionTwoEffect>(
    initialState = SubscriptionTwoState()
) {


    override fun handleEvent(event: SubscriptionTwoEvent) {
        when (event) {
            is SubscriptionTwoEvent.NavigateBack -> sendEffect(SubscriptionTwoEffect.NavigateBack)
            is SubscriptionTwoEvent.ClearError -> clearError()
        }
    }


    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
