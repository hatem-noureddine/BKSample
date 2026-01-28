package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to retrieve the timestamp of the last successful data synchronization.
 *
 * @param repository The repository to fetch sync status from.
 */
class GetLastSyncTimeUseCase(
    private val repository: BankRepository,
) {
    /**
     * Observes the last sync time.
     * @return Flow emitting the timestamp or null.
     */
    operator fun invoke(): Flow<Long?> = repository.getLastSyncTime()
}
