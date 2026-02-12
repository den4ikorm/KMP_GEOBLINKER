package org.example.geoblinker.presentation.features.settings/unit_distance




/**
 * Side-effects для единиц измерения
 */
sealed class UnitDistanceSettingsEffect {
    data object NavigateBack : UnitDistanceSettingsEffect()
    data class ShowError(val message: String) : UnitDistanceSettingsEffect()
    data class ShowMessage(val message: String) : UnitDistanceSettingsEffect()
}
