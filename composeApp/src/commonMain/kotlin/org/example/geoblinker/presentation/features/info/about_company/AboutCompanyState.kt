package org.example.geoblinker.presentation.features.info/about_company




/**
 * State для информации о компании
 */
data class AboutCompanyState(
    val isLoading: Boolean = false,
    val error: String? = null
)
