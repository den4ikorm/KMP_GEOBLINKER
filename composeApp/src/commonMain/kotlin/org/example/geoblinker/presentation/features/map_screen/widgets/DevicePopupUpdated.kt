package org.example.geoblinker.presentation.features.map_screen.widgets


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.geoblinker.core.utils.MarkerUtils
import org.example.geoblinker.domain.models.Devices


/**
 * Popup с информацией об устройстве (ОБНОВЛЕННЫЙ)
 */
@Composable
fun CustomDevicePopup(
    device: Devices,
    onDismiss: () -> Unit,
    onNavigateToDetails: () -> Unit,
    onZoomTo: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0A1628)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = device.name,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    
                    // Status indicator
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (device.isConnected == 1L) Color(0xFF00E5FF) else Color.Gray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (device.isConnected == 1L) "Онлайн" else "Оффлайн",
                            color = Color(0xFF050A18),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                
                Divider(color = Color.Gray.copy(alpha = 0.3f))
                
                
                // Info grid
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoRow(
                        icon = "🚗",
                        label = "Гос. номер",
                        value = device.registrationPlate.ifEmpty { "—" }
                    )
                    
                    
                    InfoRow(
                        icon = "📱",
                        label = "IMEI",
                        value = device.imei
                    )
                    
                    
                    InfoRow(
                        icon = "⚡",
                        label = "Скорость",
                        value = "${String.format("%.1f", device.speed * 3.6)} км/ч"
                    )
                    
                    
                    InfoRow(
                        icon = "🔋",
                        label = "Батарея",
                        value = "${device.powerRate}%"
                    )
                    
                    
                    InfoRow(
                        icon = "📡",
                        label = "Сигнал",
                        value = "${device.signalRate}%"
                    )
                    
                    
                    InfoRow(
                        icon = "📍",
                        label = "Координаты",
                        value = "${String.format("%.6f", device.lat)}, ${String.format("%.6f", device.lng)}"
                    )
                    
                    
                    InfoRow(
                        icon = MarkerUtils.getMarkerDescription(device.markerId).take(1),
                        label = "Тип",
                        value = MarkerUtils.getMarkerDescription(device.markerId)
                    )
                }
                
                
                Divider(color = Color.Gray.copy(alpha = 0.3f))
                
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onZoomTo,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF00E5FF)
                        )
                    ) {
                        Text("🎯 Приблизить")
                    }
                    
                    
                    Button(
                        onClick = onNavigateToDetails,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00E5FF),
                            contentColor = Color(0xFF050A18)
                        )
                    ) {
                        Text("Детали", fontWeight = FontWeight.Bold)
                    }
                }
                
                
                // Close button
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Закрыть", color = Color.Gray)
                }
            }
        }
    }
}


@Composable
private fun InfoRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 16.sp)
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        
        
        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
