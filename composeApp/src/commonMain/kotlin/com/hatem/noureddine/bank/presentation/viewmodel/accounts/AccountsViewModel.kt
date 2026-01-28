package com.hatem.noureddine.bank.presentation.viewmodel.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hatem.noureddine.bank.domain.model.BankSections
import com.hatem.noureddine.bank.domain.usecase.GetLastSyncTimeUseCase
import com.hatem.noureddine.bank.domain.usecase.GetSortedBanksUseCase
import com.hatem.noureddine.bank.domain.usecase.SyncBanksUseCase
import com.hatem.noureddine.bank.ui.util.DateUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the state of the Accounts screen.
 * Follows the MVI (Model-View-Intent) architectural pattern.
 *
 * @property getSortedBanksUseCase UseCase to fetch and sort bank sections.
 * @property syncBanksUseCase UseCase to trigger bank synchronization.
 * @property getLastSyncTimeUseCase UseCase to retrieve the last synchronization timestamp.
 */
class AccountsViewModel(
    private val getSortedBanksUseCase: GetSortedBanksUseCase,
    private val syncBanksUseCase: SyncBanksUseCase,
    private val getLastSyncTimeUseCase: GetLastSyncTimeUseCase,
) : ViewModel() {
    /**
     * UI State for the Accounts screen.
     *
     * @property isLoading True if a background operation is in progress (initial load).
     * @property isRefreshing True if a pull-to-refresh operation is in progress.
     * @property sections The data to be displayed, grouped by bank type.
     * @property expandedBankNames Set of bank names that are currently expanded in the UI.
     * @property error Error message, if any.
     * @property lastSyncTime Timestamp of the last successful sync.
     * @property lastUpdateFormatted Formatted string of the last sync time.
     */
    data class State(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val sections: BankSections = BankSections(emptyList(), emptyList()),
        val expandedBankNames: Set<String> = emptySet(),
        val error: String? = null,
        val lastSyncTime: Long? = null,
        val lastUpdateFormatted: String? = null,
    )

    /**
     * User Intents (Actions) that can trigger state changes.
     */
    sealed interface Intent {
        /** Validates and loads the initial data. */
        data object LoadAccounts : Intent

        /** Triggers a manual refresh of the data. */
        data object Refresh : Intent

        /**
         * Toggles the expansion state of a specific bank section.
         * @property bankName The name of the bank to toggle.
         */
        data class ToggleBankSection(
            val bankName: String,
        ) : Intent
    }

    /**
     * One-time Side Effects that are sent to the UI.
     */
    sealed interface Effect {
        /**
         * Signals that an error should be displayed to the user (e.g., via Snackbar).
         * @property message The error message to display.
         */
        data class ShowError(
            val message: String,
        ) : Effect
    }

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        observeData()
        handleIntent(Intent.LoadAccounts)
    }

    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadAccounts -> {
                viewModelScope.launch {
                    syncData(isRefresh = false)
                }
            }

            is Intent.Refresh -> {
                viewModelScope.launch {
                    syncData(isRefresh = true)
                }
            }

            is Intent.ToggleBankSection -> {
                _uiState.update { currentState ->
                    val newExpanded =
                        if (currentState.expandedBankNames.contains(intent.bankName)) {
                            currentState.expandedBankNames - intent.bankName
                        } else {
                            currentState.expandedBankNames + intent.bankName
                        }
                    currentState.copy(expandedBankNames = newExpanded)
                }
            }
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                getSortedBanksUseCase(),
                getLastSyncTimeUseCase(),
            ) { sections, lastSync ->
                _uiState.update {
                    it.copy(
                        sections = sections,
                        lastSyncTime = lastSync,
                        lastUpdateFormatted =
                            lastSync?.let { time -> DateUtils.formatDateTimeWithSeconds(time) },
                    )
                }
            }.collectLatest {
                // Collected
            }
        }
    }

    private suspend fun syncData(isRefresh: Boolean) {
        _uiState.update { state ->
            if (isRefresh) {
                state.copy(isRefreshing = true, error = null)
            } else {
                state.copy(isLoading = true, error = null)
            }
        }
        try {
            syncBanksUseCase(forceRefresh = isRefresh)
            _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
        } catch (
            @Suppress("TooGenericExceptionCaught") e: Exception,
        ) {
            _uiState.update { it.copy(isLoading = false, isRefreshing = false, error = e.message) }
            _effect.send(Effect.ShowError(e.message ?: "Unknown error"))
        }
    }
}
