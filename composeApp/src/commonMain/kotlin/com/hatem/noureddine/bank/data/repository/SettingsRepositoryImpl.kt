package com.hatem.noureddine.bank.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [SettingsRepository] using DataStore Preferences.
 *
 * @property dataStore [DataStore] instance for persisting preferences.
 */
class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {
    companion object {
        private val KEY_APP_MODE = stringPreferencesKey("app_mode")
    }

    override fun getAppMode(): Flow<AppMode> =
        dataStore.data.map { preferences ->
            val modeName = preferences[KEY_APP_MODE]
            if (modeName == null) {
                AppMode.MOCK // Final fallback
            } else {
                try {
                    AppMode.valueOf(modeName)
                } catch (@Suppress("SwallowedException", "TooGenericExceptionCaught") e: Exception) {
                    AppMode.MOCK
                }
            }
        }

    override suspend fun setAppMode(mode: AppMode) {
        dataStore.edit { preferences ->
            preferences[KEY_APP_MODE] = mode.name
        }
    }
}
