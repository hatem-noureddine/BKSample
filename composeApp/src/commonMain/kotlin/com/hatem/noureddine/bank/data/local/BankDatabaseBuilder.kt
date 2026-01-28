package com.hatem.noureddine.bank.data.local

const val DATABASE_NAME = "encrypted_band_database"

/**
 * Platform-agnostic builder for [BankDatabase].
 * Handles platform-specific database creation (e.g., SQLCipher context on Android, native path on iOS).
 */
expect class BankDatabaseBuilder() {
    /**
     * Builds the Room database instance.
     */
    fun build(): BankDatabase
}
