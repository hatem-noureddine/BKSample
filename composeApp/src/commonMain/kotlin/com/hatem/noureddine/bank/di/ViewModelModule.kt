package com.hatem.noureddine.bank.di

import com.hatem.noureddine.bank.presentation.viewmodel.accounts.AccountsViewModel
import com.hatem.noureddine.bank.presentation.viewmodel.main.MainViewModel
import com.hatem.noureddine.bank.presentation.viewmodel.operations.OperationsViewModel
import com.hatem.noureddine.bank.presentation.viewmodel.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module providing ViewModel definitions.
 * ViewModels are defined as singletons (conceptually scoped to the screen/graph in Compose).
 */
val viewModelModule =
    module {
        singleOf(::KoinDataSourceSwitcher)
        viewModelOf(::AccountsViewModel)
        viewModelOf(::SettingsViewModel)
        viewModelOf(::OperationsViewModel)
        viewModelOf(::MainViewModel)
    }
