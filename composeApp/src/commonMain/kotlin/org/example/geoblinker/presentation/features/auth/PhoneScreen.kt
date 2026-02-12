package org.example.geoblinker.presentation.features.auth


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.geoblinker.presentation.features.common.CustomTextField
import org.koin.compose.koinInject


/**
 * Экран ввода телефона (ТУПОЙ UI - только отображение state)
 */
@Composable
fun PhoneScreen(
    viewModel: AuthViewModel = koinInject(),
    onNavigateToCode: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    
    // Обработка эффектов
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AuthEffect.NavigateToCodeScreen -> onNavigateToCode()
                is AuthEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                else -> {}
            }
        }
    }
    
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF050A18) // Neon Cyber dark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "GeoBlinker",
                color = Color(0xFF00E5FF), // Neon cyan
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            
            
            Spacer(modifier = Modifier.height(48.dp))
            
            
            Text(
                text = "Введите номер телефона",
                color = Color.White,
                fontSize = 18.sp
            )
            
            
            Spacer(modifier = Modifier.height(16.dp))
            
            
            CustomTextField(
                value = state.phone,
                onValueChange = { viewModel.handleEvent(AuthEvent.OnPhoneChanged(it)) },
                placeholder = "9 XXX XXX XX XX",
                enabled = !state.isLoading
            )
            
            
            Spacer(modifier = Modifier.height(24.dp))
            
            
            Button(
                onClick = { viewModel.handleEvent(AuthEvent.OnSendCode) },
                enabled = state.isPhoneValid && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF),
                    contentColor = Color(0xFF050A18)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF050A18)
                    )
                } else {
                    Text("Получить код", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
