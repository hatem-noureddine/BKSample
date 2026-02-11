package com.hatem.noureddine.bank.presentation.viewmodel.accounts

import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.repository.BankRepository
import com.hatem.noureddine.bank.domain.usecase.GetLastSyncTimeUseCase
import com.hatem.noureddine.bank.domain.usecase.GetSortedBanksUseCase
import com.hatem.noureddine.bank.domain.usecase.SyncBanksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val fakeRepository =
        object : BankRepository {
            var lastSyncTime: Long? = 0L

            override fun getBanks() = flowOf(emptyList<Bank>())

            override fun getAccount(id: String) = flowOf(null)

            override fun getLastSyncTime() = flowOf(lastSyncTime)

            override suspend fun syncData(forceRefresh: Boolean) {
                lastSyncTime = 123456789L
            }

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
    fun `initial state loads data correctly`() =
        runTest(testDispatcher) {
            val getSortedBanks = GetSortedBanksUseCase(fakeRepository)
            val syncBanks = SyncBanksUseCase(fakeRepository)
            val getLastSyncTime = GetLastSyncTimeUseCase(fakeRepository)

            val viewModel = AccountsViewModel(getSortedBanks, syncBanks, getLastSyncTime)

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertEquals(null, state.error)
        }

    @Test
    fun `Refresh intent triggers sync`() =
        runTest(testDispatcher) {
            val getSortedBanks = GetSortedBanksUseCase(fakeRepository)
            val syncBanks = SyncBanksUseCase(fakeRepository)
            val getLastSyncTime = GetLastSyncTimeUseCase(fakeRepository)

            val viewModel = AccountsViewModel(getSortedBanks, syncBanks, getLastSyncTime)
            advanceUntilIdle()

            // Verify initial sync happened (init block)
            assertEquals(123456789L, fakeRepository.lastSyncTime)

            // Reset sync time to verify Refresh triggers it again
            fakeRepository.lastSyncTime = 0L

            // Trigger Refresh
            viewModel.handleIntent(AccountsViewModel.Intent.Refresh)

            advanceUntilIdle()

            // Verify side effect: sync called -> lastSyncTime updated
            assertEquals(123456789L, fakeRepository.lastSyncTime)
        }

    @Test
    fun `ToggleBankSection intent toggles expansion state`() =
        runTest(testDispatcher) {
            val getSortedBanks = GetSortedBanksUseCase(fakeRepository)
            val syncBanks = SyncBanksUseCase(fakeRepository)
            val getLastSyncTime = GetLastSyncTimeUseCase(fakeRepository)

            val viewModel = AccountsViewModel(getSortedBanks, syncBanks, getLastSyncTime)
            advanceUntilIdle()

            // Initial state: empty
            assertEquals(emptySet(), viewModel.uiState.value.expandedBankNames)

            // Expand "Bank A"
            viewModel.handleIntent(AccountsViewModel.Intent.ToggleBankSection("Bank A"))
            assertEquals(setOf("Bank A"), viewModel.uiState.value.expandedBankNames)

            // Collapse "Bank A"
            viewModel.handleIntent(AccountsViewModel.Intent.ToggleBankSection("Bank A"))
            assertEquals(emptySet(), viewModel.uiState.value.expandedBankNames)
        }

    @Test
    fun `Sync error emits effect and updates state`() =
        runTest(testDispatcher) {
            val errorRepository =
                object : BankRepository {
                    override fun getBanks() = flowOf(emptyList<Bank>())

                    override fun getAccount(id: String) = flowOf(null)

                    override fun getLastSyncTime() = flowOf(null)

                    override suspend fun syncData(forceRefresh: Boolean) {
                        error("Boom")
                    }

                    override suspend fun clearLastSyncTime() {}
                }

            val getSortedBanks = GetSortedBanksUseCase(errorRepository)
            val syncBanks = SyncBanksUseCase(errorRepository)
            val getLastSyncTime = GetLastSyncTimeUseCase(errorRepository)

            val viewModel = AccountsViewModel(getSortedBanks, syncBanks, getLastSyncTime)

            val effect = viewModel.effect.first()

            assertEquals(AccountsViewModel.Effect.ShowError("Boom"), effect)
            assertEquals("Boom", viewModel.uiState.value.error)
        }
}
