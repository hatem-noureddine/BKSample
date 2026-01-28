package com.hatem.noureddine.bank.data.repository

import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.test.FakePreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsRepositoryImplTest {
    @Test
    fun `getAppMode defaults to MOCK when missing`() =
        runTest {
            val dataStore = FakePreferencesDataStore()
            val repository = SettingsRepositoryImpl(dataStore)

            assertEquals(AppMode.MOCK, repository.getAppMode().first())
        }

    @Test
    fun `getAppMode defaults to MOCK when invalid`() =
        runTest {
            val key = stringPreferencesKey("app_mode")
            val dataStore = FakePreferencesDataStore(preferencesOf(key to "INVALID"))
            val repository = SettingsRepositoryImpl(dataStore)

            assertEquals(AppMode.MOCK, repository.getAppMode().first())
        }

    @Test
    fun `setAppMode persists and getAppMode emits new value`() =
        runTest {
            val dataStore = FakePreferencesDataStore()
            val repository = SettingsRepositoryImpl(dataStore)

            repository.setAppMode(AppMode.REMOTE)

            assertEquals(AppMode.REMOTE, repository.getAppMode().first())
        }
}
