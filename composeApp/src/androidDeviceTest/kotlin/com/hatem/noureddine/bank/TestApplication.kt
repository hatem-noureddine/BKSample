package com.hatem.noureddine.bank

import android.app.Application
import org.koin.androix.startup.KoinStartup
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

class TestApplication :
    Application(),
    KoinStartup {
    override fun onKoinStartup(): KoinConfiguration =
        KoinConfiguration {
            // Empty config for tests, or provide test modules here
            // We avoid starting the real app's Koin config to prevent conflicts
            modules(module { })
        }
}
