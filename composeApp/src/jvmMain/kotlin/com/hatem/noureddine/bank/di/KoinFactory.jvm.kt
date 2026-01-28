package com.hatem.noureddine.bank.di

import org.koin.core.KoinApplication

actual object KoinFactory {
    actual fun initKoin(config: KoinApplication?) {
        // No-op for JVM unit tests.
    }
}
