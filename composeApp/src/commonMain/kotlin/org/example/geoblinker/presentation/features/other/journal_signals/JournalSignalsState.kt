package org.example.geoblinker.presentation.features.other/journal_signals




/**
 * State для журнала сигналов
 */
data class JournalSignalsState(
    val isLoading: Boolean = false,
    val error: String? = null
)
