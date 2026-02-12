package org.example.geoblinker.presentation.features.subscription/subscription_two




/**
 * Side-effects для второго шага подписки
 */
sealed class SubscriptionTwoEffect {
    data object NavigateBack : SubscriptionTwoEffect()
    data class ShowError(val message: String) : SubscriptionTwoEffect()
    data class ShowMessage(val message: String) : SubscriptionTwoEffect()
}
