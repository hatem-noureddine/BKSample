package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAppModeUseCaseTest {
    private val settingsRepository = FakeSettingsRepository()
    private val useCase = GetAppModeUseCase(settingsRepository)

    @Test
    fun `invoke returns flow from repository`() =
        runTest {
            // Given
            val expectedMode = AppMode.MOCK
            settingsRepository.setAppMode(expectedMode)

            // When
            val result = useCase().first()

            // Then
            assertEquals(expectedMode, result)
        }
}

class FakeSettingsRepository : SettingsRepository {
    private val _appMode = MutableStateFlow(AppMode.REMOTE)

    override fun getAppMode(): Flow<AppMode> = _appMode

    override suspend fun setAppMode(mode: AppMode) {
        _appMode.value = mode
    }
}
