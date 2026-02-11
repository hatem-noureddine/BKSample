package com.hatem.noureddine.bank.di

import com.hatem.noureddine.bank.data.datasource.BankDataSource
import com.hatem.noureddine.bank.data.datasource.BankMockDataSourceImpl
import com.hatem.noureddine.bank.data.datasource.BankRemoteDataSourceImpl
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.domain.repository.DataSourceSwitcher
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

class KoinDataSourceSwitcher : DataSourceSwitcher {
    /**
     * Koin module providing the Remote Data Source implementation.
     */
    private val remoteDataSourceModule =
        module {
            single<BankDataSource> {
                BankRemoteDataSourceImpl(get())
            }
        }

    /**
     * Koin module providing the Mock Data Source implementation.
     */
    private val mockDataSourceModule =
        module {
            single<BankDataSource> {
                BankMockDataSourceImpl()
            }
        }

    override fun switch(mode: AppMode) {
        val oldModule = if (mode == AppMode.REMOTE) mockDataSourceModule else remoteDataSourceModule
        val newModule = if (mode == AppMode.REMOTE) remoteDataSourceModule else mockDataSourceModule

        unloadKoinModules(oldModule)
        loadKoinModules(newModule)
    }
}
