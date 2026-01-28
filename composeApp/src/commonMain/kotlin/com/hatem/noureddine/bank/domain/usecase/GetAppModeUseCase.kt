package com.hatem.noureddine.bank.domain.usecase

import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to retrieve the current application mode (Remote or Mock).
 *
 * @param settingsRepository The repository to fetch settings from.
 */
class GetAppModeUseCase(
    private val settingsRepository: SettingsRepository,
) {
    /**
     * Observes the current app mode.
     * @return Flow emitting the current [AppMode].
     */
    operator fun invoke(): Flow<AppMode> = settingsRepository.getAppMode()
}
