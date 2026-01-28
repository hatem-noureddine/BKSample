package com.hatem.noureddine.bank.di

import org.koin.core.KoinApplication

/**
 * Factory for initializing Koin dependency injection.
 * Platform-specific implementations handle the actual start-up logic.
 */
expect object KoinFactory {
    /**
     * Initializes Koin with optional configuration.
     * @param config Optional [KoinApplication] configuration.
     */
    fun initKoin(config: KoinApplication? = null)
}
