package org.example.geoblinker.presentation.features.settings/unit_distance




/**
 * События для единиц измерения
 */
sealed class UnitDistanceSettingsEvent {
    data object NavigateBack : UnitDistanceSettingsEvent()
    data object ClearError : UnitDistanceSettingsEvent()
}
