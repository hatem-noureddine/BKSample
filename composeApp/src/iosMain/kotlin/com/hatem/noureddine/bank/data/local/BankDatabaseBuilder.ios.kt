@file:Suppress("MatchingDeclarationName")

package com.hatem.noureddine.bank.data.local

import androidx.room.Room
import com.hatem.noureddine.bank.utlis.documentDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * iOS implementation for building the Room database.
 * Uses the custom [SQLCipherNativeDriver] for encryption.
 * Locates the database file in the documents directory.
 */
private class BankDatabaseBuilderIOS : BankDatabaseBuilder {
    override fun build(): BankDatabase {
        val dbFilePath = documentDirectory() + "/${DATABASE_NAME}"
        val passphrase = DatabaseKeyProvider.getDatabaseKey()
        return Room
            .databaseBuilder<BankDatabase>(
                name = dbFilePath,
                // factory = { BankDatabaseConstructor.initialize() },
            ).setDriver(SQLCipherNativeDriver(passphrase = passphrase, enableWal = true))
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}

actual fun getBankDatabaseBuilder(): BankDatabaseBuilder = BankDatabaseBuilderIOS()
