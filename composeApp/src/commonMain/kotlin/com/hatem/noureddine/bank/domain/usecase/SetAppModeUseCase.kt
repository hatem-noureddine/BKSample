package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.repository.BankRepository
import com.hatem.noureddine.bank.domain.repository.DataSourceSwitcher
import com.hatem.noureddine.bank.domain.repository.SettingsRepository

/**
 * UseCase to update the application mode (Remote or Mock).
 * Also triggers a configuration switch in the DI container or DataSources if needed.
 *
 * @param settingsRepository The repository to update settings.
 * @param bankRepository The repository to reset sync time.
 * @param dataSourceSwitcher The switcher to change data source implementation.
 */
class SetAppModeUseCase(
    private val settingsRepository: SettingsRepository,
    private val bankRepository: BankRepository,
    private val dataSourceSwitcher: DataSourceSwitcher,
) {
    /**
     * Sets the new app mode.
     * @param mode The new [AppMode] to set.
     */
    suspend operator fun invoke(mode: AppMode) {
        settingsRepository.setAppMode(mode)
        bankRepository.clearLastSyncTime()
        dataSourceSwitcher.switch(mode)
        bankRepository.syncData(forceRefresh = true)
    }
}
