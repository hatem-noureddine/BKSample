package com.hatem.noureddine.bank.domain.usecase

import androidx.annotation.OpenForTesting
import com.hatem.noureddine.bank.domain.model.BankSections
import com.hatem.noureddine.bank.domain.model.CABank
import com.hatem.noureddine.bank.domain.model.OtherBank
import com.hatem.noureddine.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * UseCase responsible for retrieving and sorting bank sections.
 * It separates banks into [CABank] and [OtherBank] categories and sorts them and their accounts.
 *
 * @param repository The repository to fetch bank data from.
 */
@OpenForTesting
class GetSortedBanksUseCase(
    private val repository: BankRepository,
) {
    /**
     * Retrieves the sorted bank sections.
     *
     * @return A [Flow] emitting [BankSections] containing sorted CA banks and Other banks.
     */
    operator fun invoke(): Flow<BankSections> =
        repository.getBanks().map { banks ->
            val ca = banks.filterIsInstance<CABank>()
            val other = banks.filterIsInstance<OtherBank>()
            BankSections(
                caBanks = ca.sortedBy { it.name }.map { it.sortAccounts() },
                otherBanks = other.sortedBy { it.name }.map { it.sortAccounts() },
            )
        }

    private fun CABank.sortAccounts(): CABank = copy(accounts = accounts.sortedBy { it.label })

    private fun OtherBank.sortAccounts(): OtherBank = copy(accounts = accounts.sortedBy { it.label })
}
