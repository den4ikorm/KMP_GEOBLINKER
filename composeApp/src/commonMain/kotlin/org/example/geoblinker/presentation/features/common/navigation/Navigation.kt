package org.example.geoblinker.presentation.features.common.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import org.example.geoblinker.core.session.SessionManager
import org.example.geoblinker.presentation.features.ListScreen
import org.example.geoblinker.presentation.features.NotificationsScreen
import org.example.geoblinker.presentation.features.auth.ConfirmationCodeScreen
import org.example.geoblinker.presentation.features.auth.PhoneScreen
import org.example.geoblinker.presentation.features.binding.BindingOneScreen
import org.example.geoblinker.presentation.features.binding.BindingThreeScreen
import org.example.geoblinker.presentation.features.binding.BindingTwoScreen
import org.example.geoblinker.presentation.features.device.DeviceDetachOneScreen
import org.example.geoblinker.presentation.features.device.DeviceDetachTwoScreen
import org.example.geoblinker.presentation.features.device.DeviceListSignalScreen
import org.example.geoblinker.presentation.features.device.DeviceThreeScreen
import org.example.geoblinker.presentation.features.device.DeviceTwoScreen
import org.example.geoblinker.presentation.features.info.AboutAppScreen
import org.example.geoblinker.presentation.features.info.AboutCompanyItemScreen
import org.example.geoblinker.presentation.features.info.AboutCompanyScreen
import org.example.geoblinker.presentation.features.info.FrequentQuestScreen
import org.example.geoblinker.presentation.features.info.FrequentQuestionsScreen
import org.example.geoblinker.presentation.features.info.IconChooserScreen
import org.example.geoblinker.presentation.features.main_screen.MainScreen
import org.example.geoblinker.presentation.features.map_screen.MapScreen
import org.example.geoblinker.presentation.features.other.ChatsScreen
import org.example.geoblinker.presentation.features.other.JournalSignalsScreen
import org.example.geoblinker.presentation.features.other.MakeRequestScreen
import org.example.geoblinker.presentation.features.settings.ConfirmationCodeSettingsScreen
import org.example.geoblinker.presentation.features.settings.DeleteAccountSettingsScreen
import org.example.geoblinker.presentation.features.settings.EmailSettingsScreen
import org.example.geoblinker.presentation.features.settings.NameSettingsScreen
import org.example.geoblinker.presentation.features.settings.NotificationSettingsScreen
import org.example.geoblinker.presentation.features.settings.PhoneSettingsScreen
import org.example.geoblinker.presentation.features.settings.UnitDistanceSettingsScreen
import org.example.geoblinker.presentation.features.subscription.SubscriptionReadyScreen
import org.example.geoblinker.presentation.features.subscription.SubscriptionTwoScreen
import org.koin.compose.koinInject


/**
 * Навигация приложения
 * Start destination определяется по наличию сессии
 */
@Composable
fun Navigation(
    navController: NavHostController
) {
    val sessionManager: SessionManager = koinInject()
    val startDestination = if (sessionManager.isAuthenticated()) MainScreen else PhoneScreen
    
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ============================================
        // AUTH FLOW
        // ============================================
        
        
        composable<PhoneScreen> {
            PhoneScreen(
                onNavigateToCode = {
                    navController.navigate(ConfirmationCodeScreen)
                }
            )
        }
        
        
        composable<ConfirmationCodeScreen> {
            ConfirmationCodeScreen(
                onNavigateToMain = {
                    navController.navigate(MainScreen) {
                        popUpTo(PhoneScreen) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        
        // ============================================
        // MAIN FLOW
        // ============================================
        
        
        composable<MainScreen> {
            MainScreen(navController = navController)
        }
        
        
        composable<MapScreen> {
            MapScreen()
        }
        
        
        composable<ListScreen> {
            ListScreen()
        }
        
        
        composable<NotificationsScreen> {
            NotificationsScreen()
        }
        
        
        // ============================================
        // BINDING FLOW
        // ============================================
        
        
        composable<BindingOneScreen> {
            BindingOneScreen(
                onNavigateToBindingTwo = { navController.navigate(BindingTwoScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<BindingTwoScreen> {
            BindingTwoScreen(
                onNavigateToBindingThree = { navController.navigate(BindingThreeScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<BindingThreeScreen> {
            BindingThreeScreen(
                onNavigateToMain = {
                    navController.navigate(MainScreen) {
                        popUpTo(BindingOneScreen) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        // ============================================
        // DEVICE FLOW
        // ============================================
        
        
        composable<DeviceListSignalScreen> {
            DeviceListSignalScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<DeviceTwoScreen> {
            DeviceTwoScreen(
                onNavigateToDeviceThree = { navController.navigate(DeviceThreeScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<DeviceThreeScreen> {
            DeviceThreeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<DeviceDetachOneScreen> {
            DeviceDetachOneScreen(
                onNavigateToDetachTwo = { navController.navigate(DeviceDetachTwoScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<DeviceDetachTwoScreen> {
            DeviceDetachTwoScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        // ============================================
        // SETTINGS FLOW
        // ============================================
        
        
        composable<NameSettingsScreen> {
            NameSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<EmailSettingsScreen> {
            EmailSettingsScreen(
                onNavigateToCode = { navController.navigate(ConfirmationCodeSettingsScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<PhoneSettingsScreen> {
            PhoneSettingsScreen(
                onNavigateToCode = { navController.navigate(ConfirmationCodeSettingsScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<ConfirmationCodeSettingsScreen> {
            ConfirmationCodeSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<NotificationSettingsScreen> {
            NotificationSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<UnitDistanceSettingsScreen> {
            UnitDistanceSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<DeleteAccountSettingsScreen> {
            DeleteAccountSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        // ============================================
        // INFO FLOW
        // ============================================
        
        
        composable<AboutAppScreen> {
            AboutAppScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<AboutCompanyScreen> {
            AboutCompanyScreen(
                onNavigateToItem = { navController.navigate(AboutCompanyItemScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<AboutCompanyItemScreen> {
            AboutCompanyItemScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<FrequentQuestionsScreen> {
            FrequentQuestionsScreen(
                onNavigateToQuestion = { navController.navigate(FrequentQuestScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<FrequentQuestScreen> {
            FrequentQuestScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<IconChooserScreen> {
            IconChooserScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        // ============================================
        // SUBSCRIPTION FLOW
        // ============================================
        
        
        composable<SubscriptionTwoScreen> {
            SubscriptionTwoScreen(
                onNavigateToReady = { navController.navigate(SubscriptionReadyScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<SubscriptionReadyScreen> {
            SubscriptionReadyScreen(
                onNavigateToMain = {
                    navController.navigate(MainScreen) {
                        popUpTo(SubscriptionTwoScreen) { inclusive = true }
                    }
                }
            )
        }
        
        
        // ============================================
        // OTHER FLOW
        // ============================================
        
        
        composable<ChatsScreen> {
            ChatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<JournalSignalsScreen> {
            JournalSignalsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        composable<MakeRequestScreen> {
            MakeRequestScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        
        // Profile, Settings и остальные экраны будут добавлены в следующих этапах
    }
}
