package org.example.geoblinker.presentation.features.other


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject


/**
 * Экран списка чатов с техподдержкой
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    onCreateNewChat: () -> Unit
) {
    val chats = remember { getSampleChats() }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Техподдержка") },
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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateNewChat,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Новое обращение"
                    )
                },
                text = { Text("Новое обращение") }
            )
        }
    ) { paddingValues ->
        if (chats.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Нет обращений",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Создайте первое обращение в поддержку",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onCreateNewChat) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Создать обращение")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(chats) { chat ->
                    ChatCard(
                        chat = chat,
                        onClick = { onNavigateToChat(chat.id) }
                    )
                }
            }
        }
    }
}


@Composable
private fun ChatCard(
    chat: ChatData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Статус индикатор
            Surface(
                modifier = Modifier.size(48.dp),
                shape = MaterialTheme.shapes.medium,
                color = when (chat.status) {
                    ChatStatus.OPEN -> MaterialTheme.colorScheme.primary
                    ChatStatus.WAITING -> MaterialTheme.colorScheme.tertiary
                    ChatStatus.CLOSED -> MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (chat.status) {
                            ChatStatus.OPEN -> Icons.Default.QuestionAnswer
                            ChatStatus.WAITING -> Icons.Default.Schedule
                            ChatStatus.CLOSED -> Icons.Default.CheckCircle
                        },
                        contentDescription = null,
                        tint = if (chat.status == ChatStatus.CLOSED) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            Color.White
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            
            // Информация о чате
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.subject,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    
                    if (chat.unreadCount > 0) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = chat.unreadCount.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                
                
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Статус
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = when (chat.status) {
                            ChatStatus.OPEN -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                            ChatStatus.WAITING -> Color(0xFFFF9800).copy(alpha = 0.1f)
                            ChatStatus.CLOSED -> Color.Gray.copy(alpha = 0.1f)
                        }
                    ) {
                        Text(
                            text = when (chat.status) {
                                ChatStatus.OPEN -> "Активен"
                                ChatStatus.WAITING -> "Ожидание"
                                ChatStatus.CLOSED -> "Закрыт"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = when (chat.status) {
                                ChatStatus.OPEN -> Color(0xFF4CAF50)
                                ChatStatus.WAITING -> Color(0xFFFF9800)
                                ChatStatus.CLOSED -> Color.Gray
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    
                    // Время
                    Text(
                        text = chat.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


// Статусы чата
private enum class ChatStatus {
    OPEN, WAITING, CLOSED
}


// Модель данных чата
private data class ChatData(
    val id: String,
    val subject: String,
    val lastMessage: String,
    val timestamp: String,
    val status: ChatStatus,
    val unreadCount: Int
)


// Примеры чатов
private fun getSampleChats(): List<ChatData> = listOf(
    ChatData(
        id = "1",
        subject = "Проблема с подключением трекера",
        lastMessage = "Спасибо! Попробую сбросить настройки.",
        timestamp = "14:35",
        status = ChatStatus.OPEN,
        unreadCount = 1
    ),
    ChatData(
        id = "2",
        subject = "Вопрос по тарифам",
        lastMessage = "Скажите, можно ли перейти с базового на премиум план?",
        timestamp = "Вчера",
        status = ChatStatus.WAITING,
        unreadCount = 0
    ),
    ChatData(
        id = "3",
        subject = "Не приходят уведомления",
        lastMessage = "Проблема решена, спасибо за помощь!",
        timestamp = "01.02.2026",
        status = ChatStatus.CLOSED,
        unreadCount = 0
    ),
    ChatData(
        id = "4",
        subject = "Ошибка при оплате подписки",
        lastMessage = "Платеж прошёл успешно. Спасибо!",
        timestamp = "31.01.2026",
        status = ChatStatus.CLOSED,
        unreadCount = 0
    ),
    ChatData(
        id = "5",
        subject = "Как экспортировать историю маршрутов?",
        lastMessage = "Наш специалист скоро вам ответит.",
        timestamp = "30.01.2026",
        status = ChatStatus.WAITING,
        unreadCount = 2
    )
)
