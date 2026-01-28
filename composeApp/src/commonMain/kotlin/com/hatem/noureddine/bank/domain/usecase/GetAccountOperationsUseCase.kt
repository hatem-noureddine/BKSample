package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.Operation
import com.hatem.noureddine.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * UseCase to retrieve a sorted list of operations for a specific account.
 * Operations are sorted by date (descending) and then by title.
 *
 * @param repository The repository to fetch account data from.
 */
class GetAccountOperationsUseCase(
    private val repository: BankRepository,
) {
    /**
     * Retrieves sorted operations for an account.
     * @param accountId The ID of the account.
     * @return Flow emitting a list of sorted operations.
     */
    operator fun invoke(accountId: String): Flow<List<Operation>> =
        repository.getAccount(accountId).map { account ->
            account?.operations?.sortedWith(
                compareByDescending<Operation> { it.date }.thenBy { it.title },
            ) ?: emptyList()
        }
}
