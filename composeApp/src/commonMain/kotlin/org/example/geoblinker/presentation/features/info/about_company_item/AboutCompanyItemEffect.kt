package org.example.geoblinker.presentation.features.info/about_company_item




/**
 * Side-effects для детальной информации о компании
 */
sealed class AboutCompanyItemEffect {
    data object NavigateBack : AboutCompanyItemEffect()
    data class ShowError(val message: String) : AboutCompanyItemEffect()
    data class ShowMessage(val message: String) : AboutCompanyItemEffect()
}
