package org.example.geoblinker.presentation.features.subscription


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.geoblinker.presentation.viewmodels.SubscriptionViewModel
import org.koin.compose.koinInject


/**
 * Экран оплаты подписки (Subscription Two)
 * Выбор способа оплаты и подтверждение покупки
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionTwoScreen(
    planId: String,
    planName: String,
    planPrice: String,
    onNavigateBack: () -> Unit,
    onNavigateToConfirmation: () -> Unit,
    viewModel: SubscriptionViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    var selectedPaymentMethod by remember { mutableStateOf("card") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SubscriptionViewModel.Effect.NavigateToConfirmation -> {
                    onNavigateToConfirmation()
                }
                is SubscriptionViewModel.Effect.ShowError -> {
                    showError = true
                }
                else -> {}
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Оплата подписки") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // План подписки
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Выбранный план",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = planName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = planPrice,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }


            // Способ оплаты
            Text(
                text = "Способ оплаты",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )


            PaymentMethodOption(
                title = "Банковская карта",
                isSelected = selectedPaymentMethod == "card",
                onClick = { selectedPaymentMethod = "card" }
            )


            PaymentMethodOption(
                title = "Google Pay",
                isSelected = selectedPaymentMethod == "google_pay",
                onClick = { selectedPaymentMethod = "google_pay" }
            )


            PaymentMethodOption(
                title = "Apple Pay",
                isSelected = selectedPaymentMethod == "apple_pay",
                onClick = { selectedPaymentMethod = "apple_pay" }
            )


            // Форма карты (если выбрана карта)
            if (selectedPaymentMethod == "card") {
                Spacer(modifier = Modifier.height(8.dp))
                
                
                Text(
                    text = "Данные карты",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )


                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { 
                        if (it.length <= 16 && it.all { char -> char.isDigit() }) {
                            cardNumber = it
                        }
                    },
                    label = { Text("Номер карты") },
                    placeholder = { Text("1234 5678 9012 3456") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = { 
                            if (it.length <= 5) {
                                expiryDate = it
                            }
                        },
                        label = { Text("Срок") },
                        placeholder = { Text("MM/YY") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )


                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { 
                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                cvv = it
                            }
                        },
                        label = { Text("CVV") },
                        placeholder = { Text("123") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }


                OutlinedTextField(
                    value = cardHolder,
                    onValueChange = { cardHolder = it },
                    label = { Text("Имя держателя") },
                    placeholder = { Text("IVAN IVANOV") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }


            // Информация о подписке
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Условия подписки:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• Автоматическое продление",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• Отмена в любое время",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• Возврат средств в течение 14 дней",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }


            if (showError) {
                Text(
                    text = "Ошибка при оплате. Проверьте данные карты.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }


            Spacer(modifier = Modifier.weight(1f))


            // Кнопка оплаты
            Button(
                onClick = {
                    if (selectedPaymentMethod == "card") {
                        if (cardNumber.length == 16 && cvv.length == 3 && 
                            expiryDate.length == 5 && cardHolder.isNotBlank()) {
                            viewModel.handleEvent(
                                SubscriptionViewModel.Event.ProcessPayment(
                                    planId = planId,
                                    paymentMethod = selectedPaymentMethod,
                                    cardData = mapOf(
                                        "number" to cardNumber,
                                        "expiry" to expiryDate,
                                        "cvv" to cvv,
                                        "holder" to cardHolder
                                    )
                                )
                            )
                        } else {
                            showError = true
                        }
                    } else {
                        viewModel.handleEvent(
                            SubscriptionViewModel.Event.ProcessPayment(
                                planId = planId,
                                paymentMethod = selectedPaymentMethod,
                                cardData = emptyMap()
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Оплатить $planPrice")
                }
            }
        }
    }
}


@Composable
private fun PaymentMethodOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Выбрано",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
