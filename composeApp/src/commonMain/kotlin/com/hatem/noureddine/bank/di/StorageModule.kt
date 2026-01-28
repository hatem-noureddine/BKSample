package com.hatem.noureddine.bank.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hatem.noureddine.bank.data.local.BankDatabase
import com.hatem.noureddine.bank.data.local.BankDatabaseBuilder
import com.hatem.noureddine.bank.data.local.DataStoreFactory
import com.hatem.noureddine.bank.data.local.dao.BankDao
import org.koin.dsl.module

/**
 * Koin module providing Storage-related dependencies.
 * Includes [DataStore], [BankDatabase], and [BankDao].
 */
val storageModule =
    module {
        single<DataStore<Preferences>> { DataStoreFactory().build() }
        single<BankDao> { get<BankDatabase>().bankDao() }
        single<BankDatabase> { BankDatabaseBuilder().build() }
    }
