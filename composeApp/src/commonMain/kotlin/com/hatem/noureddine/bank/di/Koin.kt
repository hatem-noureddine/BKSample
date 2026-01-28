package com.hatem.noureddine.bank.di

/**
 * List of Koin modules shared across all platforms.
 * Includes storage, network, repository, useCase, and viewModel modules.
 */
val sharedKoinModules =
    listOf(
        storageModule,
        dataSourceModule,
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
    )
