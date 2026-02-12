package org.example.geoblinker


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.geoblinker.presentation.features.common.navigation.Navigation
import org.example.geoblinker.presentation.theme.GeoBlinkerTheme


@Composable
fun App() {
    GeoBlinkerTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val navController = rememberNavController()
            Navigation(navController = navController)
        }
    }
}
