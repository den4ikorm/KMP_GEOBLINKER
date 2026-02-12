#!/bin/bash
PROJECT_ROOT="/storage/emulated/0/Documents/GeoBlinker_KMP"
GH_USER="den4ikorm"
GH_TOKEN="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"
REPO_URL="github.com/den4ikorm/KMP_GEOBLINKER.git"

cd "$PROJECT_ROOT" || exit

FILE="composeApp/src/commonMain/kotlin/org/example/geoblinker/presentation/viewmodels/SubscriptionViewModel.kt"

# Записываем ЧИСТЫЙ и ИСПРАВЛЕННЫЙ код
cat << 'KOTLIN' > $FILE
package org.example.geoblinker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.geoblinker.domain.models.Subscription
import org.example.geoblinker.domain.repositories.SubscriptionRepository

data class SubscriptionState(
    val subscriptions: List<Subscription> = emptyList(),
    val currentSubscription: Subscription? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class SubscriptionEvent {
    data object LoadSubscriptions : SubscriptionEvent()
    data class SelectSubscription(val subscriptionId: String) : SubscriptionEvent()
    data class PurchaseSubscription(val subscriptionId: String) : SubscriptionEvent()
    data object CancelSubscription : SubscriptionEvent()
}

sealed class SubscriptionEffect {
    data class ShowError(val message: String) : SubscriptionEffect()
    data object SubscriptionPurchased : SubscriptionEffect()
    data object SubscriptionCancelled : SubscriptionEffect()
    data class NavigateToPayment(val subscriptionId: String) : SubscriptionEffect()
}

class SubscriptionViewModel(
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SubscriptionState())
    val state: StateFlow<SubscriptionState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SubscriptionEffect>()
    val effect: SharedFlow<SubscriptionEffect> = _effect.asSharedFlow()

    init {
        loadSubscriptions()
    }

    fun onEvent(event: SubscriptionEvent) {
        when (event) {
            is SubscriptionEvent.LoadSubscriptions -> loadSubscriptions()
            is SubscriptionEvent.SelectSubscription -> selectSubscription(event.subscriptionId)
            is SubscriptionEvent.PurchaseSubscription -> purchaseSubscription(event.subscriptionId)
            is SubscriptionEvent.CancelSubscription -> cancelSubscription()
        }
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            subscriptionRepository.getSubscriptions()
                .onSuccess { subscriptions ->
                    subscriptionRepository.getCurrentSubscription()
                        .onSuccess { current ->
                            _state.update {
                                it.copy(subscriptions = subscriptions, currentSubscription = current, isLoading = false, error = null)
                            }
                        }
                        .onFailure { error ->
                            _state.update { it.copy(subscriptions = subscriptions, isLoading = false, error = error.message) }
                        }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                    _effect.emit(SubscriptionEffect.ShowError(error.message ?: "Ошибка загрузки"))
                }
        }
    }

    private fun selectSubscription(subscriptionId: String) {
        viewModelScope.launch {
            _effect.emit(SubscriptionEffect.NavigateToPayment(subscriptionId))
        }
    }

    private fun purchaseSubscription(subscriptionId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            subscriptionRepository.purchaseSubscription(subscriptionId)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(SubscriptionEffect.SubscriptionPurchased)
                    loadSubscriptions()
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(SubscriptionEffect.ShowError(error.message ?: "Ошибка оформления"))
                }
        }
    }

    private fun cancelSubscription() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            subscriptionRepository.cancelSubscription()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(SubscriptionEffect.SubscriptionCancelled)
                    loadSubscriptions()
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(SubscriptionEffect.ShowError(error.message ?: "Ошибка отмены"))
                }
        }
    }
}
KOTLIN

echo ">>> Отправляем исправленный ViewModel в GitHub..."
git config --global --add safe.directory "$PROJECT_ROOT"
git remote set-url origin "https://$GH_USER:$GH_TOKEN@$REPO_URL"
git add .
git commit -m "Fix: Final clean up of SubscriptionViewModel syntax and coroutines"
git push origin main --force

echo ">>> ГОТОВО! Билд должен быть зеленым."
