package com.hatem.noureddine.bank.presentation.viewmodel.settings

import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.repository.SettingsRepository
import com.hatem.noureddine.bank.domain.usecase.GetAppModeUseCase
import com.hatem.noureddine.bank.domain.usecase.SetAppModeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private class FakeSettingsRepository : SettingsRepository {
        private val modeFlow = MutableStateFlow(AppMode.MOCK)
        var lastSet: AppMode? = null

        override fun getAppMode(): StateFlow<AppMode> = modeFlow

        override suspend fun setAppMode(mode: AppMode) {
            lastSet = mode
            modeFlow.value = mode
        }
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
    fun `observeSettings updates UI state`() =
        runTest(testDispatcher) {
            val repo = FakeSettingsRepository()
            val bankRepo =
                object : com.hatem.noureddine.bank.domain.repository.BankRepository {
                    override fun getBanks() = flowOf(emptyList<Bank>())

                    override fun getAccount(id: String) = flowOf(null)

                    override fun getLastSyncTime() = flowOf(null)

                    @Suppress("EmptyFunctionBlock")
                    override suspend fun syncData(forceRefresh: Boolean) {
                    }

                    @Suppress("EmptyFunctionBlock")
                    override suspend fun clearLastSyncTime() {
                    }
                }
            val dataSourceSwitcher =
                object : com.hatem.noureddine.bank.domain.repository.DataSourceSwitcher {
                    @Suppress("EmptyFunctionBlock")
                    override fun switch(mode: AppMode) {
                    }
                }
            val viewModel =
                SettingsViewModel(
                    getAppModeUseCase = GetAppModeUseCase(repo),
                    setAppModeUseCase = SetAppModeUseCase(repo, bankRepo, dataSourceSwitcher),
                )

            advanceUntilIdle()

            assertEquals(AppMode.MOCK, viewModel.uiState.value.currentMode)
        }

    @Test
    fun `ChangeMode intent delegates to use case`() =
        runTest(testDispatcher) {
            val repo = FakeSettingsRepository()
            val bankRepo =
                object : com.hatem.noureddine.bank.domain.repository.BankRepository {
                    override fun getBanks() = kotlinx.coroutines.flow.flowOf(emptyList<Bank>())

                    override fun getAccount(id: String) = kotlinx.coroutines.flow.flowOf(null)

                    override fun getLastSyncTime() = kotlinx.coroutines.flow.flowOf(null)

                    @Suppress("EmptyFunctionBlock")
                    override suspend fun syncData(forceRefresh: Boolean) {
                    }

                    @Suppress("EmptyFunctionBlock")
                    override suspend fun clearLastSyncTime() {
                    }
                }
            val dataSourceSwitcher =
                object : com.hatem.noureddine.bank.domain.repository.DataSourceSwitcher {
                    @Suppress("EmptyFunctionBlock")
                    override fun switch(mode: AppMode) {
                    }
                }
            val viewModel =
                SettingsViewModel(
                    getAppModeUseCase = GetAppModeUseCase(repo),
                    setAppModeUseCase = SetAppModeUseCase(repo, bankRepo, dataSourceSwitcher),
                )

            viewModel.handleIntent(SettingsViewModel.Intent.ChangeMode(AppMode.REMOTE))
            advanceUntilIdle()

            assertEquals(AppMode.REMOTE, repo.lastSet)
            assertEquals(AppMode.REMOTE, viewModel.uiState.value.currentMode)
        }
}
