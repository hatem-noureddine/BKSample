package com.hatem.noureddine.bank.presentation.viewmodel.operations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Operation
import com.hatem.noureddine.bank.domain.usecase.GetAccountDetailsUseCase
import com.hatem.noureddine.bank.ui.util.DateUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SECONDS_TO_MILLIS = 1000L

/**
 * ViewModel for the Operations (Transactions) screen.
 * manages the state of a specific account's operations list.
 *
 * @property getAccountDetailsUseCase UseCase to fetch account details and operations.
 */
class OperationsViewModel(
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
) : ViewModel() {
    /**
     * UI State for the Operations screen.
     *
     * @property isLoading True if data is being fetched.
     * @property account The account details associated with these operations.
     * @property operations Flattened list of operations.
     * @property groupedOperations Operations grouped by date headers.
     * @property error Error message if fetching fails.
     */
    data class State(
        val isLoading: Boolean = false,
        val account: Account? = null,
        val operations: List<Operation> = emptyList(),
        val groupedOperations: Map<String, List<Operation>> = emptyMap(),
        val error: String? = null,
    )

    /**
     * User Intents for the Operations screen.
     */
    sealed interface Intent {
        /**
         * Load operations for a specific account.
         * @property accountId The ID of the account.
         */
        data class LoadAccount(
            val accountId: String,
        ) : Intent
    }

    /**
     * One-time side effects for the Operations screen.
     */
    sealed interface Effect {
        /**
         * Show an error message.
         * @property message The error text.
         */
        data class ShowError(
            val message: String,
        ) : Effect
    }

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect: SharedFlow<Effect> = _effect

    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadAccount -> loadAccount(intent.accountId)
        }
    }

    private fun loadAccount(accountId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getAccountDetailsUseCase(accountId)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                    _effect.emit(Effect.ShowError(e.message ?: "Unknown Error"))
                }.collect { account ->
                    if (account != null) {
                        val sortedOps =
                            account.operations.sortedWith(
                                Comparator { op1, op2 ->
                                    val date1 = DateUtils.getHeaderDate(op1.date * SECONDS_TO_MILLIS)
                                    val date2 = DateUtils.getHeaderDate(op2.date * SECONDS_TO_MILLIS)

                                    if (date1 != date2) {
                                        op2.date.compareTo(op1.date)
                                    } else {
                                        op1.title.compareTo(op2.title, ignoreCase = true)
                                    }
                                },
                            )
                        val sortedAccount = account.copy(operations = sortedOps)
                        val groupedOps =
                            sortedOps.groupBy { DateUtils.getHeaderDate(it.date * SECONDS_TO_MILLIS) }

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                account = sortedAccount,
                                operations = sortedOps,
                                groupedOperations = groupedOps,
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, error = "Account not found") }
                    }
                }
        }
    }
}
