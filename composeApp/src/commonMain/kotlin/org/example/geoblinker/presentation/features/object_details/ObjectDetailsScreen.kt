package org.example.geoblinker.presentation.features.object_details


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


/**
 * ObjectDetailsScreen - ТОЛЬКО UI
 * Вся логика в ObjectDetailsViewModel!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectDetailsScreen(
    deviceId: String,
    viewModel: ObjectDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    
    // Загрузка устройства при открытии экрана
    LaunchedEffect(deviceId) {
        viewModel.onEvent(ObjectDetailsEvent.OnLoadDevice(deviceId))
    }
    
    
    // Обработка эффектов
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ObjectDetailsEffect.NavigateBack -> onNavigateBack()
                is ObjectDetailsEffect.ShowSuccess -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is ObjectDetailsEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }
    
    
    Scaffold(
        topBar = {
            TopBar(
                state = state,
                onEvent = viewModel::onEvent
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> LoadingContent()
                state.error != null -> ErrorContent(
                    error = state.error!!,
                    onRetry = { viewModel.onEvent(ObjectDetailsEvent.OnRetry) }
                )
                state.device != null -> DeviceDetailsContent(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
    
    
    // Диалог удаления
    if (state.showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = { viewModel.onEvent(ObjectDetailsEvent.OnDeleteConfirmed) },
            onDismiss = { viewModel.onEvent(ObjectDetailsEvent.OnDeleteDialogDismissed) }
        )
    }
}


/**
 * TopBar с кнопками действий
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    state: ObjectDetailsState,
    onEvent: (ObjectDetailsEvent) -> Unit
) {
    TopAppBar(
        title = { 
            Text(
                text = state.device?.name ?: "Детали устройства"
            ) 
        },
        navigationIcon = {
            IconButton(onClick = { onEvent(ObjectDetailsEvent.OnBackClicked) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            if (state.device != null) {
                if (state.isEditing) {
                    // Режим редактирования
                    IconButton(
                        onClick = { onEvent(ObjectDetailsEvent.OnSaveClicked) },
                        enabled = !state.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save"
                        )
                    }
                } else {
                    // Режим просмотра
                    IconButton(onClick = { onEvent(ObjectDetailsEvent.OnEditClicked) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                    
                    
                    IconButton(onClick = { onEvent(ObjectDetailsEvent.OnDeleteClicked) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    )
}


/**
 * Контент загрузки
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


/**
 * Контент ошибки
 */
@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Red,
            textAlign = TextAlign.Center
        )
        
        
        Spacer(Modifier.height(16.dp))
        
        
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}


/**
 * Контент деталей устройства
 */
@Composable
private fun DeviceDetailsContent(
    state: ObjectDetailsState,
    onEvent: (ObjectDetailsEvent) -> Unit
) {
    val device = state.device ?: return
    
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Название
        DetailItem(
            label = "Название",
            value = device.name,
            isEditable = state.isEditing,
            onValueChange = { onEvent(ObjectDetailsEvent.OnNameChanged(it)) }
        )
        
        
        // IMEI
        DetailItem(
            label = "IMEI",
            value = device.imei,
            isEditable = false
        )
        
        
        // Регистрационный номер
        if (device.registrationPlate.isNotEmpty()) {
            DetailItem(
                label = "Регистрационный номер",
                value = device.registrationPlate,
                isEditable = false
            )
        }
        
        
        // Тип устройства
        DetailItem(
            label = "Тип устройства",
            value = device.type,
            isEditable = false
        )
        
        
        // Координаты
        DetailItem(
            label = "Координаты",
            value = "${device.lat}, ${device.lng}",
            isEditable = false
        )
        
        
        // Статус подключения
        DetailItem(
            label = "Статус",
            value = if (device.isConnected) "Подключено" else "Отключено",
            isEditable = false
        )
        
        
        // Уровень заряда
        if (device.batteryLevel > 0) {
            DetailItem(
                label = "Уровень заряда",
                value = "${device.batteryLevel}%",
                isEditable = false
            )
        }
        
        
        // Уровень сигнала
        if (device.signalStrength > 0) {
            DetailItem(
                label = "Уровень сигнала",
                value = "${device.signalStrength}%",
                isEditable = false
            )
        }
        
        
        // Дата привязки
        DetailItem(
            label = "Дата привязки",
            value = formatDate(device.bindingTime),
            isEditable = false
        )
    }
}


/**
 * Элемент детали
 */
@Composable
private fun DetailItem(
    label: String,
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit = {}
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        
        
        Spacer(Modifier.height(4.dp))
        
        
        if (isEditable) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}


/**
 * Диалог подтверждения удаления
 */
@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удалить устройство?") },
        text = { 
            Text("Это действие нельзя отменить. Устройство будет удалено из системы.") 
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}


/**
 * Форматирование даты
 */
private fun formatDate(timestamp: Long): String {
    return try {
        val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(timestamp)
        val localDateTime = instant.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
        
        
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.monthNumber.toString().padStart(2, '0')
        val year = localDateTime.year
        
        
        "$day.$month.$year"
    } catch (e: Exception) {
        "Неизвестно"
    }
}
