package org.example.geoblinker.presentation.features.info


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject


/**
 * Экран выбора иконки для устройства
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconChooserScreen(
    currentIconId: String = "default",
    deviceName: String = "",
    onNavigateBack: () -> Unit,
    onIconSelected: (String) -> Unit
) {
    var selectedIconId by remember { mutableStateOf(currentIconId) }
    val icons = remember { getAvailableIcons() }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Выбор иконки") },
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
            // Информация об устройстве
            if (deviceName.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val selectedIcon = icons.find { it.id == selectedIconId }
                        
                        
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = selectedIcon?.icon ?: Icons.Default.Place,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        
                        
                        Column {
                            Text(
                                text = deviceName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = selectedIcon?.name ?: "Стандартная",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }


            Text(
                text = "Выберите иконку для устройства:",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )


            // Сетка иконок
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(icons) { iconData ->
                    IconItem(
                        iconData = iconData,
                        isSelected = iconData.id == selectedIconId,
                        onClick = { selectedIconId = iconData.id }
                    )
                }
            }


            // Кнопка применения
            Button(
                onClick = {
                    onIconSelected(selectedIconId)
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Применить")
            }
        }
    }
}


@Composable
private fun IconItem(
    iconData: DeviceIconData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder().copy(
                width = 2.dp,
                brush = androidx.compose.ui.graphics.SolidColor(
                    MaterialTheme.colorScheme.primary
                )
            )
        } else {
            null
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconData.icon,
                contentDescription = iconData.name,
                modifier = Modifier.size(32.dp),
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}


// Модель данных иконки
private data class DeviceIconData(
    val id: String,
    val name: String,
    val icon: ImageVector
)


// Список доступных иконок
private fun getAvailableIcons(): List<DeviceIconData> = listOf(
    // Транспорт
    DeviceIconData("car", "Автомобиль", Icons.Default.DirectionsCar),
    DeviceIconData("motorcycle", "Мотоцикл", Icons.Default.TwoWheeler),
    DeviceIconData("truck", "Грузовик", Icons.Default.LocalShipping),
    DeviceIconData("bus", "Автобус", Icons.Default.DirectionsBus),
    DeviceIconData("bike", "Велосипед", Icons.Default.DirectionsBike),
    DeviceIconData("train", "Поезд", Icons.Default.Train),
    DeviceIconData("boat", "Лодка", Icons.Default.DirectionsBoat),
    DeviceIconData("flight", "Самолёт", Icons.Default.Flight),
    
    
    // Люди и животные
    DeviceIconData("person", "Человек", Icons.Default.Person),
    DeviceIconData("child", "Ребёнок", Icons.Default.ChildCare),
    DeviceIconData("elderly", "Пожилой", Icons.Default.Accessible),
    DeviceIconData("pet", "Питомец", Icons.Default.Pets),
    
    
    // Объекты
    DeviceIconData("backpack", "Рюкзак", Icons.Default.ShoppingBag),
    DeviceIconData("luggage", "Багаж", Icons.Default.Luggage),
    DeviceIconData("briefcase", "Портфель", Icons.Default.Work),
    DeviceIconData("key", "Ключ", Icons.Default.Key),
    
    
    // Устройства
    DeviceIconData("phone", "Телефон", Icons.Default.Phone),
    DeviceIconData("tablet", "Планшет", Icons.Default.TabletAndroid),
    DeviceIconData("laptop", "Ноутбук", Icons.Default.Computer),
    DeviceIconData("watch", "Часы", Icons.Default.Watch),
    
    
    // Места
    DeviceIconData("home", "Дом", Icons.Default.Home),
    DeviceIconData("work", "Работа", Icons.Default.Business),
    DeviceIconData("school", "Школа", Icons.Default.School),
    DeviceIconData("hospital", "Больница", Icons.Default.LocalHospital),
    DeviceIconData("store", "Магазин", Icons.Default.Store),
    DeviceIconData("restaurant", "Ресторан", Icons.Default.Restaurant),
    
    
    // Спорт и активности
    DeviceIconData("run", "Бег", Icons.Default.DirectionsRun),
    DeviceIconData("fitness", "Фитнес", Icons.Default.FitnessCenter),
    DeviceIconData("hiking", "Поход", Icons.Default.Hiking),
    DeviceIconData("golf", "Гольф", Icons.Default.GolfCourse),
    
    
    // Разное
    DeviceIconData("star", "Звезда", Icons.Default.Star),
    DeviceIconData("favorite", "Избранное", Icons.Default.Favorite),
    DeviceIconData("bookmark", "Закладка", Icons.Default.Bookmark),
    DeviceIconData("location", "Локация", Icons.Default.LocationOn),
    DeviceIconData("navigation", "Навигация", Icons.Default.Navigation),
    DeviceIconData("explore", "Исследование", Icons.Default.Explore),
    DeviceIconData("flag", "Флаг", Icons.Default.Flag),
    DeviceIconData("emergency", "Экстренная служба", Icons.Default.LocalPolice)
)
