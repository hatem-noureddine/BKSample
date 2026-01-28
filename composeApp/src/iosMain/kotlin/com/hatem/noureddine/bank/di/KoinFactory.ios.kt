package com.hatem.noureddine.bank.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

/**
 * iOS implementation of Koin initialization.
 * Configures Koin with shared modules.
 */
actual object KoinFactory {
    actual fun initKoin(config: KoinApplication?) {
        startKoin {
            modules(sharedKoinModules)
        }
    }
}
