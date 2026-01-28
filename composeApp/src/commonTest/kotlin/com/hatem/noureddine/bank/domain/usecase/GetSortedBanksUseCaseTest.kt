package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.model.CABank
import com.hatem.noureddine.bank.domain.model.OtherBank
import com.hatem.noureddine.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSortedBanksUseCaseTest {
    private val fakeRepository =
        object : BankRepository {
            override fun getBanks(): Flow<List<Bank>> =
                flowOf(
                    listOf(
                        OtherBank(
                            name = "Other Bank",
                            accounts =
                                listOf(
                                    createAccount("Z Account"),
                                    createAccount("A Account"),
                                ),
                        ),
                        CABank(
                            name = "CA Bank",
                            accounts =
                                listOf(
                                    createAccount("D Account"),
                                    createAccount("B Account"),
                                ),
                        ),
                    ),
                )

            override suspend fun syncData(forceRefresh: Boolean) {}

            override fun getAccount(id: String): Flow<Account?> = flowOf(null)

            override fun getLastSyncTime(): Flow<Long?> = flowOf(null)

            override suspend fun clearLastSyncTime() {}
        }

    private fun createAccount(label: String) =
        Account(
            id = "id",
            order = 1,
            holder = "holder",
            role = 1,
            contractNumber = "123",
            label = label,
            productCode = "code",
            balance = 100.0,
            operations = emptyList(),
        )

    @Test
    fun `should separate CA and Other banks and sort accounts alphabetically`() =
        runTest {
            val useCase = GetSortedBanksUseCase(fakeRepository)
            val result = useCase().first()

            // Check separation
            assertEquals(1, result.caBanks.size, "Should have 1 CA bank")
            assertEquals(1, result.otherBanks.size, "Should have 1 Other bank")
            assertEquals("CA Bank", result.caBanks.first().name)
            assertEquals("Other Bank", result.otherBanks.first().name)

            // Check account sorting (CA)
            val caAccounts = result.caBanks.first().accounts
            assertEquals("B Account", caAccounts[0].label)
            assertEquals("D Account", caAccounts[1].label)

            // Check account sorting (Other)
            val otherAccounts = result.otherBanks.first().accounts
            assertEquals("A Account", otherAccounts[0].label)
            assertEquals("Z Account", otherAccounts[1].label)
        }
}
