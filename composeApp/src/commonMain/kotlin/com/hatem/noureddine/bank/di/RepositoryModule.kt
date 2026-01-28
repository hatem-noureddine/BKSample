package com.hatem.noureddine.bank.di

import com.hatem.noureddine.bank.data.repository.BankRepositoryImpl
import com.hatem.noureddine.bank.data.repository.SettingsRepositoryImpl
import com.hatem.noureddine.bank.domain.repository.BankRepository
import com.hatem.noureddine.bank.domain.repository.SettingsRepository
import org.koin.dsl.module

/**
 * Koin module providing Repository implementations.
 * Binds interfaces to their implementations (e.g., [BankRepository] to [BankRepositoryImpl]).
 */
val repositoryModule =
    module {
        single<SettingsRepository> { SettingsRepositoryImpl(get()) }

        factory<BankRepository> {
            BankRepositoryImpl(
                bankDao = get(),
                dataStore = get(),
                dataSourceProvider = { get() },
            )
        }
    }
