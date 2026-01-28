package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SyncBanksUseCaseTest {
    @Test
    fun `invoke delegates to repository`() =
        runTest {
            var lastForceRefresh: Boolean? = null
            val repository =
                object : BankRepository {
                    override fun getBanks() = flowOf(emptyList<Bank>())

                    override fun getAccount(id: String) = flowOf<Account?>(null)

                    override fun getLastSyncTime() = flowOf(0L)

                    override suspend fun syncData(forceRefresh: Boolean) {
                        lastForceRefresh = forceRefresh
                    }

                    override suspend fun clearLastSyncTime() {}
                }

            val useCase = SyncBanksUseCase(repository)

            useCase(forceRefresh = true)

            assertEquals(true, lastForceRefresh)
        }
}
