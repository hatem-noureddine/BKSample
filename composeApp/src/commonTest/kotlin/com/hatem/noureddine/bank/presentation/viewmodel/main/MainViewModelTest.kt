package com.hatem.noureddine.bank.presentation.viewmodel.main

import com.hatem.noureddine.bank.domain.repository.DataSourceSwitcher
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.repository.SettingsRepository
import com.hatem.noureddine.bank.domain.usecase.GetAppModeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
class MainViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private class FakeSettingsRepository : SettingsRepository {
        val modeFlow = MutableStateFlow(AppMode.MOCK)

        override fun getAppMode(): Flow<AppMode> = modeFlow

        override suspend fun setAppMode(mode: AppMode) {}
    }

    private class FakeSwitcher : DataSourceSwitcher {
        var lastMode: AppMode? = null

        override fun switch(mode: AppMode) {
            lastMode = mode
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
    fun `Initialize observes app mode and triggers switch`() =
        runTest(testDispatcher) {
            val repo = FakeSettingsRepository()
            val switcher = FakeSwitcher()
            val viewModel = MainViewModel(GetAppModeUseCase(repo), switcher)

            advanceUntilIdle()

            assertEquals(AppMode.MOCK, viewModel.uiState.value.appMode)
            assertEquals(AppMode.MOCK, switcher.lastMode)
        }
}
