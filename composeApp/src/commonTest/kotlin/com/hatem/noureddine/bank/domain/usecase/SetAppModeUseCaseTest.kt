package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.repository.BankRepository
import com.hatem.noureddine.bank.domain.repository.DataSourceSwitcher
import com.hatem.noureddine.bank.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SetAppModeUseCaseTest {
    @Test
    fun `invoke delegates to repository`() =
        runTest {
            var storedMode: AppMode? = null
            val repository =
                object : SettingsRepository {
                    override fun getAppMode(): Flow<AppMode> = flowOf(AppMode.MOCK)

                    override suspend fun setAppMode(mode: AppMode) {
                        storedMode = mode
                    }
                }

            val bankRepository =
                object : BankRepository {
                    override fun getBanks(): Flow<List<Bank>> = flowOf(emptyList())

                    override fun getAccount(id: String): Flow<Account?> = flowOf(null)

                    override fun getLastSyncTime(): Flow<Long?> = flowOf(null)

                    override suspend fun syncData(forceRefresh: Boolean) {}

                    override suspend fun clearLastSyncTime() {}
                }

            val dataSourceSwitcher =
                object : DataSourceSwitcher {
                    var lastSwitchedMode: AppMode? = null

                    override fun switch(mode: AppMode) {
                        lastSwitchedMode = mode
                    }
                }

            val useCase = SetAppModeUseCase(repository, bankRepository, dataSourceSwitcher)

            useCase(AppMode.REMOTE)

            assertEquals(AppMode.REMOTE, storedMode)
            assertEquals(AppMode.REMOTE, dataSourceSwitcher.lastSwitchedMode)
        }
}
