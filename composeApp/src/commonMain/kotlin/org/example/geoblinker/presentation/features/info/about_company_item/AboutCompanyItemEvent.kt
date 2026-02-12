package org.example.geoblinker.presentation.features.info/about_company_item




/**
 * События для детальной информации о компании
 */
sealed class AboutCompanyItemEvent {
    data object NavigateBack : AboutCompanyItemEvent()
    data object ClearError : AboutCompanyItemEvent()
}
