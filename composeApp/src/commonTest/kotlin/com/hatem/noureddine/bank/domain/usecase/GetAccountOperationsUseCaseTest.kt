package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.model.Operation
import com.hatem.noureddine.bank.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GetAccountOperationsUseCaseTest {
    private val operations =
        listOf(
            Operation(id = "1", title = "B Operation", amount = 10.0, category = "cat", date = 1000L),
            // Same date, should be sorted by title
            Operation(id = "2", title = "A Operation", amount = 20.0, category = "cat", date = 1000L),
            Operation(
                id = "3",
                title = "C Operation",
                amount = 30.0,
                category = "cat",
                date = 2000L,
            ), // Newer date, should be first
        )

    private val fakeAccount =
        Account(
            id = "acc1",
            order = 1,
            holder = "holder",
            role = 1,
            contractNumber = "123",
            label = "label",
            productCode = "code",
            balance = 100.0,
            operations = operations,
        )

    private val fakeRepository =
        object : BankRepository {
            override fun getBanks(): Flow<List<Bank>> = flowOf(emptyList())

            override fun getAccount(id: String): Flow<Account?> = if (id == "acc1") flowOf(fakeAccount) else flowOf(null)

            override suspend fun syncData(forceRefresh: Boolean) {}

            override fun getLastSyncTime(): Flow<Long?> = flowOf(null)

            override suspend fun clearLastSyncTime() {}
        }

    @Test
    fun `should sort operations by date descending then title alphabetical`() =
        runTest {
            val useCase = GetAccountOperationsUseCase(fakeRepository)
            val result = useCase("acc1").first()

            assertNotNull(result)
            assertEquals(3, result.size)

            // C Operation (2000L) -> First
            assertEquals("C Operation", result[0].title)

            // A Operation (1000L) < B Operation (1000L) alphabetically
            // Spec RG06: "Si deux opérations ont la même date, afficher par ordre alphabétique"
            assertEquals("A Operation", result[1].title)
            assertEquals("B Operation", result[2].title)
        }

    @Test
    fun `returns empty list when account is missing`() =
        runTest {
            val useCase = GetAccountOperationsUseCase(fakeRepository)
            val result = useCase("unknown").first()

            assertEquals(emptyList(), result)
        }
}
