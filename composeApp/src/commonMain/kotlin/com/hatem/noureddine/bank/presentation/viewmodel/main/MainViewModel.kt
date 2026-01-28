package com.hatem.noureddine.bank.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hatem.noureddine.bank.domain.repository.DataSourceSwitcher
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.usecase.GetAppModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Main screen/Activity.
 * Responsible for initializing global app state, such as observing the AppMode.
 *
 * @property getAppModeUseCase UseCase to observe the current application mode.
 */
class MainViewModel(
    private val getAppModeUseCase: GetAppModeUseCase,
    private val dataSourceSwitcher: DataSourceSwitcher,
) : ViewModel() {
    /**
     * UI State for the Main screen.
     * @property appMode The current [AppMode] (Remote or Mock).
     */
    data class State(
        val appMode: AppMode? = null,
    )

    /**
     * User Intents for the Main screen.
     */
    sealed interface Intent {
        /** Initialize the ViewModel and start observing app mode. */
        data object Initialize : Intent
    }

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    init {
        handleIntent(Intent.Initialize)
    }

    fun handleIntent(intent: Intent) {
        when (intent) {
            Intent.Initialize -> observeAppMode()
        }
    }

    private fun observeAppMode() {
        viewModelScope.launch {
            getAppModeUseCase().collectLatest { mode ->
                if (_uiState.value.appMode != mode) {
                    dataSourceSwitcher.switch(mode)
                    _uiState.update { it.copy(appMode = mode) }
                }
            }
        }
    }
}
