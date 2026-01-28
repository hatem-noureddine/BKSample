package com.hatem.noureddine.bank.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hatem.noureddine.bank.data.datasource.BankDataSource
import com.hatem.noureddine.bank.data.dto.BankDto
import com.hatem.noureddine.bank.data.local.dao.BankDao
import com.hatem.noureddine.bank.data.mapper.toDomain
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

/**
 * Implementation of [BankRepository] that follows an Offline-First strategy.
 * It uses [BankDao] as the single source of truth and [BankDataSource] for network fetching.
 *
 * @property bankDao The local database Data Access Object.
 * @property dataStore The DataStore preferences for storing simple key-value pairs (like sync time).
 * @property dataSource The remote data source for fetching bank data.
 */
class BankRepositoryImpl(
    private val bankDao: BankDao,
    private val dataStore: DataStore<Preferences>,
    private val dataSourceProvider: () -> BankDataSource,
) : BankRepository {
    companion object {
        private val KEY_LAST_SYNC_TIME = stringPreferencesKey("last_sync_time")
        private const val MIN_SYNC_INTERVAL_MS = 5 * 60 * 1000L
    }

    override fun getBanks(): Flow<List<Bank>> =
        bankDao.getBanksWithAccounts().map { entities ->
            entities.map { it.toDomain() }
        }

    /**
     * Retrieves an account by its ID from the local database.
     * @param id The unique identifier of the account.
     * @return Flow emitting the Account or null if not found.
     */
    override fun getAccount(id: String): Flow<Account?> = bankDao.getAccountWithOperations(id).map { it?.toDomain() }

    /**
     * Observes the timestamp of the last successful data synchronization.
     * @return Flow emitting the timestamp (epoch millis) or null.
     */
    override fun getLastSyncTime(): Flow<Long?> =
        dataStore.data.map { preferences ->
            preferences[KEY_LAST_SYNC_TIME]?.toLongOrNull()
        }

    /**
     * Synchronizes local data with the remote source.
     * - Fetches data from network
     * - Maps DTOs to Entities
     * - Replaces local database content transactionally
     * - Updates last sync time in DataStore
     *
     * @param forceRefresh If true, bypasses the cache throttle and forces a network refresh.
     */
    @kotlin.time.ExperimentalTime
    override suspend fun syncData(forceRefresh: Boolean) {
        if (!forceRefresh) {
            val lastSyncTime = dataStore.data.first()[KEY_LAST_SYNC_TIME]?.toLongOrNull()
            if (lastSyncTime != null) {
                val now = Clock.System.now().toEpochMilliseconds()
                if (now - lastSyncTime < MIN_SYNC_INTERVAL_MS) {
                    return
                }
            }
        }

        val response: List<BankDto> = dataSourceProvider().fetchBanks()

        // Use Mapper
        val mappedData =
            com.hatem.noureddine.bank.data.mapper.BankDtoMapper
                .mapToEntities(response)

        // Transactional update
        bankDao.replaceAll(mappedData.bankEntities, mappedData.accountEntities, mappedData.operationEntities)

        // Update timestamp
        val nowStr =
            Clock.System
                .now()
                .toEpochMilliseconds()
                .toString()
        dataStore.edit { preferences ->
            preferences[KEY_LAST_SYNC_TIME] = nowStr
        }
    }
    override suspend fun clearLastSyncTime() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_LAST_SYNC_TIME)
        }
    }
}
