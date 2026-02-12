package org.example.geoblinker.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.geoblinker.domain.models.PaymentResponseData
import org.example.geoblinker.domain.models.SubscriptionInfo
import org.example.geoblinker.domain.repositories.SubscriptionRepository


/**
 * Состояние экрана подписок
 */
sealed class SubscriptionUiState {
    data object Loading : SubscriptionUiState()
    data class Success(
        val subscriptions: List<SubscriptionInfo>,
        val activeSubscription: SubscriptionInfo?
    ) : SubscriptionUiState()
    data class Error(val message: String) : SubscriptionUiState()
}


/**
 * Состояние тарифов
 */
sealed class TariffUiState {
    data object Loading : TariffUiState()
    data class Success(val tariffs: Map<String, Map<String, Any>>) : TariffUiState()
    data class Error(val message: String) : TariffUiState()
}


/**
 * Состояние создания платежа
 */
sealed class PaymentCreationState {
    data object Idle : PaymentCreationState()
    data object Loading : PaymentCreationState()
    data class Success(val paymentData: PaymentResponseData) : PaymentCreationState()
    data class Error(val message: String) : PaymentCreationState()
}


/**
 * ViewModel для управления подписками
 * 
 * Отвечает за:
 * - Загрузку списка подписок пользователя
 * - Получение списка доступных тарифов
 * - Создание новой подписки
 * - Создание платежа
 * - Отмену подписки
 */
class SubscriptionViewModel(
    private val repository: SubscriptionRepository
) : ViewModel() {
    
    
    // Состояние подписок
    private val _subscriptionState = MutableStateFlow<SubscriptionUiState>(SubscriptionUiState.Loading)
    val subscriptionState: StateFlow<SubscriptionUiState> = _subscriptionState.asStateFlow()
    
    
    // Состояние тарифов
    private val _tariffState = MutableStateFlow<TariffUiState>(TariffUiState.Loading)
    val tariffState: StateFlow<TariffUiState> = _tariffState.asStateFlow()
    
    
    // Состояние создания платежа
    private val _paymentCreationState = MutableStateFlow<PaymentCreationState>(PaymentCreationState.Idle)
    val paymentCreationState: StateFlow<PaymentCreationState> = _paymentCreationState.asStateFlow()
    
    
    // Флаг обновления данных
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()


    init {
        // Автоматически загружаем данные при создании ViewModel
        loadSubscriptions()
        loadTariffs()
    }
    
    
    /**
     * Загрузить список подписок пользователя
     */
    fun loadSubscriptions() {
        viewModelScope.launch {
            _subscriptionState.value = SubscriptionUiState.Loading
            
            
            repository.getUserSubscriptions()
                .onSuccess { subscriptions ->
                    val activeSubscription = findActiveSubscription(subscriptions)
                    _subscriptionState.value = SubscriptionUiState.Success(
                        subscriptions = subscriptions,
                        activeSubscription = activeSubscription
                    )
                }
                .onFailure { error ->
                    _subscriptionState.value = SubscriptionUiState.Error(
                        message = error.message ?: "Failed to load subscriptions"
                    )
                }
        }
    }
    
    
    /**
     * Загрузить список доступных тарифов
     */
    fun loadTariffs() {
        viewModelScope.launch {
            _tariffState.value = TariffUiState.Loading
            
            
            repository.getTariffs()
                .onSuccess { tariffs ->
                    _tariffState.value = TariffUiState.Success(tariffs = tariffs)
                }
                .onFailure { error ->
                    _tariffState.value = TariffUiState.Error(
                        message = error.message ?: "Failed to load tariffs"
                    )
                }
        }
    }
    
    
    /**
     * Обновить данные (pull-to-refresh)
     */
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            
            
            try {
                // Загружаем подписки и тарифы параллельно
                loadSubscriptions()
                loadTariffs()
            } finally {
                _isRefreshing.value = false
            }
        }
    }
    
    
    /**
     * Создать новую подписку
     * 
     * @param tariffId ID выбранного тарифа
     */
    fun createSubscription(tariffId: String) {
        viewModelScope.launch {
            _subscriptionState.value = SubscriptionUiState.Loading
            
            
            repository.createSubscription(tariffId)
                .onSuccess { subsId ->
                    println("SubscriptionViewModel: Subscription created with ID: $subsId")
                    // Перезагружаем список подписок
                    loadSubscriptions()
                }
                .onFailure { error ->
                    _subscriptionState.value = SubscriptionUiState.Error(
                        message = error.message ?: "Failed to create subscription"
                    )
                }
        }
    }
    
    
    /**
     * Создать платеж для подписки
     * 
     * @param amount Сумма платежа
     * @param subsId ID подписки (опционально)
     * @param appUrl URL для возврата после оплаты (опционально)
     */
    fun createPayment(
        amount: Int,
        subsId: String? = null,
        appUrl: String? = null
    ) {
        viewModelScope.launch {
            _paymentCreationState.value = PaymentCreationState.Loading
            
            
            repository.createPayment(amount, subsId, appUrl)
                .onSuccess { paymentData ->
                    println("SubscriptionViewModel: Payment created: ${paymentData.pId}")
                    _paymentCreationState.value = PaymentCreationState.Success(paymentData)
                }
                .onFailure { error ->
                    _paymentCreationState.value = PaymentCreationState.Error(
                        message = error.message ?: "Failed to create payment"
                    )
                }
        }
    }
    
    
    /**
     * Сбросить состояние создания платежа
     */
    fun resetPaymentCreationState() {
        _paymentCreationState.value = PaymentCreationState.Idle
    }
    
    
    /**
     * Отменить подписку
     * 
     * @param subsId ID подписки для отмены
     */
    fun cancelSubscription(subsId: String) {
        viewModelScope.launch {
            // Здесь должна быть логика отмены подписки через API
            // Пока что просто перезагружаем данные
            println("SubscriptionViewModel: Cancelling subscription: $subsId")
            loadSubscriptions()
        }
    }
    
    
    /**
     * Найти активную подписку из списка
     */
    private fun findActiveSubscription(subscriptions: List<SubscriptionInfo>): SubscriptionInfo? {
        val currentTime = System.currentTimeMillis()
        
        
        return subscriptions.firstOrNull { subscription ->
            subscription.subsStatus == "active" &&
            subscription.endDate > currentTime &&
            subscription.paid
        }
    }
    
    
    /**
     * Проверить, есть ли активная подписка
     */
    fun hasActiveSubscription(): Boolean {
        return when (val state = _subscriptionState.value) {
            is SubscriptionUiState.Success -> state.activeSubscription != null
            else -> false
        }
    }
    
    
    /**
     * Получить оставшиеся дни подписки
     */
    fun getRemainingDays(): Int? {
        val state = _subscriptionState.value
        if (state is SubscriptionUiState.Success) {
            val activeSubscription = state.activeSubscription ?: return null
            val currentTime = System.currentTimeMillis()
            val remainingMillis = activeSubscription.endDate - currentTime
            return (remainingMillis / (1000 * 60 * 60 * 24)).toInt()
        }
        return null
    }
    
    
    /**
     * Очистить состояния при уничтожении
     */
    override fun onCleared() {
        super.onCleared()
        println("SubscriptionViewModel: Cleared")
    }
}
