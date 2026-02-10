@file:Suppress("MatchingDeclarationName")

package com.hatem.noureddine.bank.data.local

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import org.koin.mp.KoinPlatform

/**
 * Android implementation for building the Room database.
 * Uses SQLCipher for database encryption.
 *
 * It retrieves the context from Koin, loads the native library, and configures
 * the database builder with the required passphrase and factories.
 */
private class BankDatabaseBuilderAndroid : BankDatabaseBuilder {
    override fun build(): BankDatabase {
        val context = KoinPlatform.getKoin().get<Context>()

        // load SQL Cipher to encrypt database
        System.loadLibrary("sqlcipher")

        val dbPath = context.getDatabasePath(DATABASE_NAME)
        val passphrase = DatabaseKeyProvider.getDatabaseKey().toByteArray()

        val factory = SupportOpenHelperFactory(passphrase)
        return Room
            .databaseBuilder<BankDatabase>(
                context = context.applicationContext,
                name = dbPath.absolutePath,
            ).openHelperFactory(factory)
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}

actual fun getBankDatabaseBuilder(): BankDatabaseBuilder = BankDatabaseBuilderAndroid()
