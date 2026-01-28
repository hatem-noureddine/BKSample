package com.hatem.noureddine.bank.di

import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level

/**
 * Android implementation of Koin initialization.
 * Configures Koin with Android logger and shared modules.
 */
actual object KoinFactory {
    actual fun initKoin(config: KoinApplication?) {
        config?.androidLogger(Level.DEBUG)
        config?.modules(sharedKoinModules)
    }
}
