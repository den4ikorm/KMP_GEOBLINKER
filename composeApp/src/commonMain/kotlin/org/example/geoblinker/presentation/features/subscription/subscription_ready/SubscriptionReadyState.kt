package org.example.geoblinker.presentation.features.subscription/subscription_ready




/**
 * State для завершения подписки
 */
data class SubscriptionReadyState(
    val isLoading: Boolean = false,
    val error: String? = null
)
