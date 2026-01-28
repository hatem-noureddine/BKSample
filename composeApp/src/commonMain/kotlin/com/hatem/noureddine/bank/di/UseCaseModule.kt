package com.hatem.noureddine.bank.di

import com.hatem.noureddine.bank.domain.usecase.GetAccountDetailsUseCase
import com.hatem.noureddine.bank.domain.usecase.GetAppModeUseCase
import com.hatem.noureddine.bank.domain.usecase.GetLastSyncTimeUseCase
import com.hatem.noureddine.bank.domain.usecase.GetSortedBanksUseCase
import com.hatem.noureddine.bank.domain.usecase.SetAppModeUseCase
import com.hatem.noureddine.bank.domain.usecase.SyncBanksUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin module providing UseCase definitions.
 * All UseCases are defined as singletons.
 */
val useCaseModule =
    module {
        singleOf(::GetSortedBanksUseCase)
        singleOf(::SyncBanksUseCase)
        singleOf(::GetLastSyncTimeUseCase)
        singleOf(::GetAccountDetailsUseCase)
        singleOf(::GetAppModeUseCase)
        singleOf(::SetAppModeUseCase)
    }
