package com.hatem.noureddine.bank.di

import com.hatem.noureddine.bank.domain.repository.DataSourceSwitcher
import org.koin.dsl.module

/**
 * Koin module providing Data Source implementations.
 */
val dataSourceModule =
    module {
        single<DataSourceSwitcher> { KoinDataSourceSwitcher() }
    }
