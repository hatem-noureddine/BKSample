package com.hatem.noureddine.bank.domain.repository

import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Bank
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing Bank data.
 * Defines the contract for data operations related to Banks, Accounts, and Syncing.
 */
interface BankRepository {
    /**
     * Observes the list of all available banks.
     * @return A [Flow] emitting a list of [Bank] entities.
     */
    fun getBanks(): Flow<List<Bank>>

    /**
     * Observes a specific account by its ID.
     * @param id The unique identifier of the account.
     * @return A [Flow] emitting the [Account] or null if not found.
     */
    fun getAccount(id: String): Flow<Account?>

    /**
     * Observes the last synchronization timestamp.
     * @return A [Flow] emitting the timestamp in epoch milliseconds, or null if never synced.
     */
    fun getLastSyncTime(): Flow<Long?>

    /**
     * Triggers a data synchronization.
     * Fetches data from the remote source and updates the local cache.
     *
     * @param forceRefresh If true, forces a network fetch even if cache is fresh.
     */
    suspend fun syncData(forceRefresh: Boolean = false)

    /**
     * Clears the last synchronization timestamp.
     * Use this when switching data sources or environments to force a fresh fetch.
     */
    suspend fun clearLastSyncTime()
}
