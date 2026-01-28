package com.hatem.noureddine.bank.data.local

actual class BankDatabaseBuilder {
    actual fun build(): BankDatabase {
        throw UnsupportedOperationException("BankDatabase is not available on JVM tests.")
    }
}
