package com.hatem.noureddine.bank.domain.repository

import com.hatem.noureddine.bank.domain.model.AppMode
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing application settings.
 * Handles persistence and retrieval of user preferences like App Mode.
 */
interface SettingsRepository {
    /**
     * Observes the current application mode.
     * @return A [Flow] emitting the current [AppMode].
     */
    fun getAppMode(): Flow<AppMode>

    /**
     * Updates the application mode.
     * @param mode The new [AppMode] to set.
     */
    suspend fun setAppMode(mode: AppMode)
}
