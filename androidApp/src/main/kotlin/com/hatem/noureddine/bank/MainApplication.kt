package com.hatem.noureddine.bank

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.logger.Level
import org.koin.dsl.KoinConfiguration

class MainApplication :
    Application(),
    KoinStartup {
    override fun onKoinStartup(): KoinConfiguration =
        KoinConfiguration {
            androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
        }
}
