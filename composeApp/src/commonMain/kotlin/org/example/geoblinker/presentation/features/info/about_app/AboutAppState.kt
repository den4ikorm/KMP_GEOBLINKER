package org.example.geoblinker.presentation.features.info/about_app




/**
 * State для информации о приложении
 */
data class AboutAppState(
    val isLoading: Boolean = false,
    val error: String? = null
)
