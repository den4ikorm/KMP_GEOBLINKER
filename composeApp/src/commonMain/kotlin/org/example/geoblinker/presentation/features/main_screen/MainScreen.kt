package org.example.geoblinker.presentation.features.main_screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.time.Clock
import org.example.geoblinker.presentation.features.main_screen.widgets.LoadingOverlay
import org.example.geoblinker.presentation.features.main_screen.widgets.SubscriptionExpiredDialog
import org.example.geoblinker.presentation.features.main_screen.widgets.TopBar
import org.example.geoblinker.presentation.features.map_screen.MapScreen
import org.example.geoblinker.presentation.viewmodels.AvatarViewModel
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.example.geoblinker.presentation.viewmodels.ProfileViewModel
import org.example.geoblinker.presentation.viewmodels.states.DefaultStates
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MainScreen(
    onNavigateToSubscription: () -> Unit,
    onNavigateToBinding: () -> Unit,
    onNavigateToDevice: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToList: () -> Unit
) {
    val deviceViewModel = koinViewModel<DeviceViewModel>()
    val profileViewModel = koinViewModel<ProfileViewModel>()
    val avatarViewModel = koinViewModel<AvatarViewModel>()


    val deviceState by deviceViewModel.state.collectAsState()
    val profileState by profileViewModel.state.collectAsState()
    val avatarState by avatarViewModel.state.collectAsState()


    val countNotifications = remember(deviceState.signals, deviceState.news) {
        deviceState.signals.size + deviceState.news.size -
                (deviceState.signals.count { it.isSeen == 1L } + deviceState.news.count { it.isSeen == 1L })
    }


    val isSubscriptionExpired = remember(profileState) {
        profileState.isLogin && profileState.subscriptionEndDate < Clock.System.now().toEpochMilliseconds()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 90.dp)
    ) {
        MapScreen(
            viewModel = deviceViewModel,
            toBindingScreen = onNavigateToBinding,
            toDeviceScreen = { device -> onNavigateToDevice(device.imei) }
        )
    }


    TopBar(
        isMapSelected = true,
        isListSelected = false,
        countNotifications = countNotifications,
        avatarUri = avatarState.avatarUri,
        onProfileClick = onNavigateToProfile,
        onMapClick = { /* Уже на карте */ },
        onListClick = onNavigateToList,
        onNotificationsClick = onNavigateToNotifications
    )


    if (deviceState.uiState is DefaultStates.Loading) {
        LoadingOverlay()
    }


    if (isSubscriptionExpired) {
        SubscriptionExpiredDialog(
            onPayClick = onNavigateToSubscription
        )
    }
}
