package org.example.geoblinker.presentation.features.info/about_company




/**
 * События для информации о компании
 */
sealed class AboutCompanyEvent {
    data object NavigateBack : AboutCompanyEvent()
    data object ClearError : AboutCompanyEvent()
}
