package org.example.geoblinker.presentation.features.info


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject


/**
 * Экран списка частых вопросов (FAQ)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequentQuestionsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToQuestion: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val questions = remember { getFAQQuestions() }
    
    
    val filteredQuestions = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            questions
        } else {
            questions.filter { 
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Частые вопросы") },
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
        ) {
            // Поиск
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Поиск по вопросам...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск"
                    )
                },
                singleLine = true
            )


            // Категории вопросов
            val categorizedQuestions = filteredQuestions.groupBy { it.category }
            
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                categorizedQuestions.forEach { (category, questionsInCategory) ->
                    item {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    
                    items(questionsInCategory) { question ->
                        QuestionCard(
                            question = question,
                            onClick = { onNavigateToQuestion(question.id) }
                        )
                    }
                }
                
                
                if (filteredQuestions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Вопросы не найдены",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun QuestionCard(
    question: FAQQuestion,
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = question.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = question.preview,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
            
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Открыть",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}


// Модель вопроса
private data class FAQQuestion(
    val id: String,
    val category: String,
    val title: String,
    val preview: String
)


// Данные вопросов
private fun getFAQQuestions(): List<FAQQuestion> = listOf(
    // Начало работы
    FAQQuestion(
        id = "getting_started_1",
        category = "Начало работы",
        title = "Как начать использовать GeoBlinker?",
        preview = "Простые шаги для начала работы с приложением..."
    ),
    FAQQuestion(
        id = "getting_started_2",
        category = "Начало работы",
        title = "Как привязать первое устройство?",
        preview = "Инструкция по привязке GPS-трекера к вашему аккаунту..."
    ),
    FAQQuestion(
        id = "getting_started_3",
        category = "Начало работы",
        title = "Какие устройства поддерживаются?",
        preview = "Список совместимых GPS-трекеров и моделей..."
    ),
    
    
    // Устройства
    FAQQuestion(
        id = "devices_1",
        category = "Устройства",
        title = "Сколько устройств можно подключить?",
        preview = "Лимиты на количество устройств в разных тарифах..."
    ),
    FAQQuestion(
        id = "devices_2",
        category = "Устройства",
        title = "Как отвязать устройство?",
        preview = "Пошаговая инструкция по отвязке трекера..."
    ),
    FAQQuestion(
        id = "devices_3",
        category = "Устройства",
        title = "Что делать если устройство не в сети?",
        preview = "Возможные причины и решения проблем с подключением..."
    ),
    FAQQuestion(
        id = "devices_4",
        category = "Устройства",
        title = "Как продлить заряд батареи трекера?",
        preview = "Советы по оптимизации работы устройства..."
    ),
    
    
    // Уведомления
    FAQQuestion(
        id = "notifications_1",
        category = "Уведомления",
        title = "Как настроить уведомления?",
        preview = "Настройка push и email уведомлений..."
    ),
    FAQQuestion(
        id = "notifications_2",
        category = "Уведомления",
        title = "Почему не приходят уведомления?",
        preview = "Проверка настроек и решение проблем..."
    ),
    FAQQuestion(
        id = "notifications_3",
        category = "Уведомления",
        title = "Как настроить геозоны?",
        preview = "Создание зон и настройка оповещений при входе/выходе..."
    ),
    
    
    // Подписка и оплата
    FAQQuestion(
        id = "subscription_1",
        category = "Подписка и оплата",
        title = "Какие есть тарифные планы?",
        preview = "Обзор доступных подписок и их возможностей..."
    ),
    FAQQuestion(
        id = "subscription_2",
        category = "Подписка и оплата",
        title = "Как отменить подписку?",
        preview = "Инструкция по отмене автоматического продления..."
    ),
    FAQQuestion(
        id = "subscription_3",
        category = "Подписка и оплата",
        title = "Можно ли вернуть деньги?",
        preview = "Политика возврата средств и условия..."
    ),
    FAQQuestion(
        id = "subscription_4",
        category = "Подписка и оплата",
        title = "Какие способы оплаты доступны?",
        preview = "Список поддерживаемых методов оплаты..."
    ),
    
    
    // Аккаунт
    FAQQuestion(
        id = "account_1",
        category = "Аккаунт",
        title = "Как изменить email или телефон?",
        preview = "Обновление контактных данных в профиле..."
    ),
    FAQQuestion(
        id = "account_2",
        category = "Аккаунт",
        title = "Как восстановить пароль?",
        preview = "Процедура восстановления доступа к аккаунту..."
    ),
    FAQQuestion(
        id = "account_3",
        category = "Аккаунт",
        title = "Как удалить аккаунт?",
        preview = "Полное удаление аккаунта и всех данных..."
    ),
    
    
    // Карта и история
    FAQQuestion(
        id = "map_1",
        category = "Карта и история",
        title = "Как посмотреть историю маршрутов?",
        preview = "Просмотр пройденных маршрутов и треков..."
    ),
    FAQQuestion(
        id = "map_2",
        category = "Карта и история",
        title = "Можно ли экспортировать данные?",
        preview = "Экспорт истории перемещений и отчетов..."
    ),
    FAQQuestion(
        id = "map_3",
        category = "Карта и история",
        title = "Как часто обновляется местоположение?",
        preview = "Интервалы обновления для разных тарифов..."
    ),
    
    
    // Техническая поддержка
    FAQQuestion(
        id = "support_1",
        category = "Техническая поддержка",
        title = "Как связаться с поддержкой?",
        preview = "Способы связи с технической поддержкой..."
    ),
    FAQQuestion(
        id = "support_2",
        category = "Техническая поддержка",
        title = "Время работы службы поддержки",
        preview = "График работы и среднее время ответа..."
    ),
    FAQQuestion(
        id = "support_3",
        category = "Техническая поддержка",
        title = "Как сообщить об ошибке?",
        preview = "Процедура отправки bug report..."
    )
)
