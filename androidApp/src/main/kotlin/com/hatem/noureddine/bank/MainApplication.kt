package com.hatem.noureddine.bank

import android.app.Application
import com.hatem.noureddine.bank.di.KoinFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.dsl.KoinConfiguration

class MainApplication :
    Application(),
    KoinStartup {
    override fun onKoinStartup(): KoinConfiguration =
        KoinConfiguration {
            androidContext(this@MainApplication)
            KoinFactory.initKoin(this)
        }
}
