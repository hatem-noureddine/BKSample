package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.repository.BankRepository

/**
 * UseCase responsible for triggering the synchronization of bank data.
 *
 * @param repository The repository to delegate the sync operation to.
 */
class SyncBanksUseCase(
    private val repository: BankRepository,
) {
    /**
     * Executes the synchronization logic.
     *
     * @param forceRefresh If true, forces a data refresh from the remote source even if local cache is valid.
     */
    suspend operator fun invoke(forceRefresh: Boolean = false) = repository.syncData(forceRefresh)
}
