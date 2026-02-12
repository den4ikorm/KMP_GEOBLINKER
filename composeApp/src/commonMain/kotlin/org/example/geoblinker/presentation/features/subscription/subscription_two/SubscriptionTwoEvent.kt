package org.example.geoblinker.presentation.features.subscription/subscription_two




/**
 * События для второго шага подписки
 */
sealed class SubscriptionTwoEvent {
    data object NavigateBack : SubscriptionTwoEvent()
    data object ClearError : SubscriptionTwoEvent()
}
