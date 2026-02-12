package org.example.geoblinker.presentation.features.info/about_company




/**
 * Side-effects для информации о компании
 */
sealed class AboutCompanyEffect {
    data object NavigateBack : AboutCompanyEffect()
    data class ShowError(val message: String) : AboutCompanyEffect()
    data class ShowMessage(val message: String) : AboutCompanyEffect()
}
