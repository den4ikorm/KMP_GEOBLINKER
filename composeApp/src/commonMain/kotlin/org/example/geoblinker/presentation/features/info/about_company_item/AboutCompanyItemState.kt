package org.example.geoblinker.presentation.features.info/about_company_item




/**
 * State для детальной информации о компании
 */
data class AboutCompanyItemState(
    val isLoading: Boolean = false,
    val error: String? = null
)
