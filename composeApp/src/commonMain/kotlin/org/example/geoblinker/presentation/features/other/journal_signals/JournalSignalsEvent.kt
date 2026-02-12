package org.example.geoblinker.presentation.features.other/journal_signals




/**
 * События для журнала сигналов
 */
sealed class JournalSignalsEvent {
    data object NavigateBack : JournalSignalsEvent()
    data object ClearError : JournalSignalsEvent()
}
