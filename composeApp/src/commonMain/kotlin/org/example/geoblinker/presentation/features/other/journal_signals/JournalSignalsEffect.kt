package org.example.geoblinker.presentation.features.other/journal_signals




/**
 * Side-effects для журнала сигналов
 */
sealed class JournalSignalsEffect {
    data object NavigateBack : JournalSignalsEffect()
    data class ShowError(val message: String) : JournalSignalsEffect()
    data class ShowMessage(val message: String) : JournalSignalsEffect()
}
