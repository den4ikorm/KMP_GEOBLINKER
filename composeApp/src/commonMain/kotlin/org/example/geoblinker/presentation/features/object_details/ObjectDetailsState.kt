package org.example.geoblinker.presentation.features.object_details


import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.geoblinker.domain.models.Devices


/**
 * State для ObjectDetailsScreen
 * Полностью immutable и platform-safe
 */
data class ObjectDetailsState(
    val device: Devices? = null,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val error: String? = null
)
