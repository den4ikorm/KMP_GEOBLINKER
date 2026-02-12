package org.example.geoblinker.presentation.features.subscription/subscription_ready




/**
 * События для завершения подписки
 */
sealed class SubscriptionReadyEvent {
    data object NavigateBack : SubscriptionReadyEvent()
    data object ClearError : SubscriptionReadyEvent()
}
