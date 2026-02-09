package com.hatem.noureddine.bank.presentation.viewmodel.operations

import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.model.Operation
import com.hatem.noureddine.bank.domain.repository.BankRepository
import com.hatem.noureddine.bank.domain.usecase.GetAccountDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class OperationsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val fakeAccount =
        Account(
            id = "acc1",
            label = "Test Account",
            balance = 100.0,
            operations =
                listOf(
                    Operation("op1", "Operation 1", 10.0, "cat", 1672531200L), // 2023-01-01
                    Operation("op2", "Operation 2", 20.0, "cat", 1672617600L), // 2023-01-02
                ),
            order = 1,
            holder = "Holder",
            role = 1,
            contractNumber = "123",
            productCode = "CODE",
        )

    private val fakeRepository =
        object : BankRepository {
            override fun getBanks() = flowOf(emptyList<Bank>())

            override fun getAccount(id: String) = if (id == "acc1") flowOf(fakeAccount) else flowOf(null)

            override fun getLastSyncTime() = flowOf(0L)

            override suspend fun syncData(forceRefresh: Boolean) {}

            override suspend fun clearLastSyncTime() {}
        }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `LoadAccount intent loads account and sorts operations`() =
        runTest(testDispatcher) {
            val getAccountDetails = GetAccountDetailsUseCase(fakeRepository)
            val viewModel = OperationsViewModel(getAccountDetails)

            viewModel.handleIntent(OperationsViewModel.Intent.LoadAccount("acc1"))

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNotNull(state.account)
            assertEquals("Test Account", state.account.label)

            // operations should be sorted by date descending in VM logic
            assertEquals(2, state.operations.size)
            assertEquals("Operation 2", state.operations[0].title) // Newer date first

            // Grouped operations verification
            // This depends on DateUtils implementation, limiting check to map size for stability
            assertEquals(2, state.groupedOperations.size)
        }

    @Test
    fun `LoadAccount intent handles missing account`() =
        runTest(testDispatcher) {
            val getAccountDetails = GetAccountDetailsUseCase(fakeRepository)
            val viewModel = OperationsViewModel(getAccountDetails)

            viewModel.handleIntent(OperationsViewModel.Intent.LoadAccount("unknown"))

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals("Account not found", state.error)
        }

    @Test
    fun `LoadAccount intent emits effect on error`() =
        runTest(testDispatcher) {
            val errorRepository =
                object : BankRepository {
                    override fun getBanks() = flowOf(emptyList<Bank>())

                    override fun getAccount(id: String) =
                        flow<Account?> {
                            throw IllegalStateException("Boom")
                        }

                    override fun getLastSyncTime() = flowOf(0L)

                    override suspend fun syncData(forceRefresh: Boolean) {}

                    override suspend fun clearLastSyncTime() {}
                }

            val getAccountDetails = GetAccountDetailsUseCase(errorRepository)
            val viewModel = OperationsViewModel(getAccountDetails)

            viewModel.handleIntent(OperationsViewModel.Intent.LoadAccount("acc1"))

            val effect = viewModel.effect.first()

            assertEquals(OperationsViewModel.Effect.ShowError("Boom"), effect)
            assertEquals("Boom", viewModel.uiState.value.error)
        }

    @Test
    fun `LoadAccount sorts operations by day descending then title ascending`() =
        runTest(testDispatcher) {
            // Day 1: 1672531200L (2023-01-01)
            // Day 2: 1672617600L (2023-01-02)
            val day1At10am = 1672567200L
            val day1At12pm = 1672574400L
            val day2 = 1672617600L

            val operations = listOf(
                Operation("1", "Z_Title", 10.0, "cat", day1At12pm), // Day 1, later time
                Operation("2", "A_Title", 10.0, "cat", day1At10am), // Day 1, earlier time
                Operation("3", "B_Title", 10.0, "cat", day2)       // Day 2
            )
            // Expected Sort:
            // 1. Day 2 (B_Title) - Newest Day
            // 2. Day 1 (A_Title) - Alphabetical A
            // 3. Day 1 (Z_Title) - Alphabetical Z (even though timestamp is later than A)

            val sortedAccount = fakeAccount.copy(operations = operations)
            
            val mockRepo = object : BankRepository {
                override fun getBanks() = flowOf(emptyList<Bank>())
                override fun getAccount(id: String) = flowOf(sortedAccount)
                override fun getLastSyncTime() = flowOf(0L)
                override suspend fun syncData(forceRefresh: Boolean) {}
                override suspend fun clearLastSyncTime() {}
            }

            val viewModel = OperationsViewModel(GetAccountDetailsUseCase(mockRepo))
            viewModel.handleIntent(OperationsViewModel.Intent.LoadAccount("acc1"))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            val resultTitles = state.operations.map { it.title }
            
            assertEquals(listOf("B_Title", "A_Title", "Z_Title"), resultTitles)
        }


    @Test
    fun `LoadAccount loads account with empty operations`() =
        runTest(testDispatcher) {
            val emptyAccount = fakeAccount.copy(operations = emptyList())
            val mockRepo = object : BankRepository {
                override fun getBanks() = flowOf(emptyList<Bank>())
                override fun getAccount(id: String) = flowOf(emptyAccount)
                override fun getLastSyncTime() = flowOf(0L)
                override suspend fun syncData(forceRefresh: Boolean) {}
                override suspend fun clearLastSyncTime() {}
            }

            val viewModel = OperationsViewModel(GetAccountDetailsUseCase(mockRepo))
            viewModel.handleIntent(OperationsViewModel.Intent.LoadAccount("acc1"))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertNotNull(state.account)
            assertEquals(0, state.operations.size)
            assertEquals(0, state.groupedOperations.size)
        }
}
