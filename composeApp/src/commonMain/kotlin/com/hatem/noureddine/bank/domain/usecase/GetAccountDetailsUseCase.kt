package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to retrieve the details of a specific account.
 *
 * @param bankRepository The repository to fetch account data from.
 */
class GetAccountDetailsUseCase(
    private val bankRepository: BankRepository,
) {
    /**
     * Retrieves the account by ID.
     * @param accountId The ID of the account to fetch.
     * @return Flow emitting the Account.
     */
    operator fun invoke(accountId: String): Flow<Account?> = bankRepository.getAccount(accountId)
}
