package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetLastSyncTimeUseCaseTest {
    @Test
    fun `invoke returns last sync time from repository`() =
        runTest {
            val repository =
                object : BankRepository {
                    override fun getBanks() = flowOf(emptyList<Bank>())

                    override fun getAccount(id: String) = flowOf<Account?>(null)

                    override fun getLastSyncTime() = flowOf(1234L)

                    override suspend fun syncData(forceRefresh: Boolean) {}

                    override suspend fun clearLastSyncTime() {}
                }

            val useCase = GetLastSyncTimeUseCase(repository)

            assertEquals(1234L, useCase().first())
        }
}
