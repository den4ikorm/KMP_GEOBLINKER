package org.example.geoblinker.presentation.features.subscription/subscription_ready




/**
 * Side-effects для завершения подписки
 */
sealed class SubscriptionReadyEffect {
    data object NavigateBack : SubscriptionReadyEffect()
    data class ShowError(val message: String) : SubscriptionReadyEffect()
    data class ShowMessage(val message: String) : SubscriptionReadyEffect()
}
