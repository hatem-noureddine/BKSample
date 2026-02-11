@file:Suppress("MatchingDeclarationName")

package com.hatem.noureddine.bank.data.local

class BankDatabaseBuilderJVM : BankDatabaseBuilder {
    override fun build(): BankDatabase =
        throw UnsupportedOperationException(
            "BankDatabase is not available on JVM tests.",
        )
}

actual fun getBankDatabaseBuilder(): BankDatabaseBuilder = BankDatabaseBuilderJVM()
