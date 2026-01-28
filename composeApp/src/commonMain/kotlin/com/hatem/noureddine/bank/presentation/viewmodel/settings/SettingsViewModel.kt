package com.hatem.noureddine.bank.presentation.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.usecase.GetAppModeUseCase
import com.hatem.noureddine.bank.domain.usecase.SetAppModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Settings screen.
 * Manages the application mode configuration (switching between Remote and Mock).
 *
 * @property getAppModeUseCase UseCase to observe the current mode.
 * @property setAppModeUseCase UseCase to change the mode.
 */
class SettingsViewModel(
    private val getAppModeUseCase: GetAppModeUseCase,
    private val setAppModeUseCase: SetAppModeUseCase,
) : ViewModel() {
    /**
     * UI State for the Settings screen.
     * @property currentMode The currently active [AppMode].
     */
    data class State(
        val currentMode: AppMode? = null,
    )

    /**
     * User Intents for the Settings screen.
     */
    sealed interface Intent {
        /**
         * Change the application mode.
         * @property mode The new mode to set.
         */
        data class ChangeMode(
            val mode: AppMode,
        ) : Intent
    }

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.ChangeMode -> {
                onSwitchMode(intent.mode)
            }
        }
    }

    private fun onSwitchMode(mode: AppMode) {
        viewModelScope.launch {
            setAppModeUseCase(mode)
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            getAppModeUseCase().collectLatest { mode ->
                _uiState.update { it.copy(currentMode = mode) }
            }
        }
    }
}
