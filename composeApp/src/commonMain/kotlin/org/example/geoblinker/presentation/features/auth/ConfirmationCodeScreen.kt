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
 * Экран ввода кода подтверждения
 */
@Composable
fun ConfirmationCodeScreen(
    viewModel: AuthViewModel = koinInject(),
    onNavigateToMain: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AuthEffect.NavigateToMain -> onNavigateToMain()
                is AuthEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                else -> {}
            }
        }
    }
    
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF050A18)
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
                text = "Код подтверждения",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            
            Spacer(modifier = Modifier.height(8.dp))
            
            
            Text(
                text = "Отправлен на номер ${state.phone}",
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            
            Spacer(modifier = Modifier.height(32.dp))
            
            
            CustomTextField(
                value = state.code,
                onValueChange = { viewModel.handleEvent(AuthEvent.OnCodeChanged(it)) },
                placeholder = "000000",
                enabled = !state.isLoading
            )
            
            
            Spacer(modifier = Modifier.height(24.dp))
            
            
            Button(
                onClick = { viewModel.handleEvent(AuthEvent.OnConfirmCode) },
                enabled = state.isCodeValid && !state.isLoading,
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
                    Text("Подтвердить", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            
            Spacer(modifier = Modifier.height(16.dp))
            
            
            TextButton(onClick = onNavigateBack) {
                Text("Изменить номер", color = Color(0xFF00E5FF))
            }
        }
    }
}
