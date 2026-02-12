package org.example.geoblinker.presentation.features.subscription/subscription_two




/**
 * State для второго шага подписки
 */
data class SubscriptionTwoState(
    val isLoading: Boolean = false,
    val error: String? = null
)
