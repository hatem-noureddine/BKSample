package com.hatem.noureddine.bank.data.local

/**
 * Provides the SQLCipher database key from a secure source.
 */
object DatabaseKeyProvider {
    /**
     * Returns a non-empty SQLCipher key.
     */
    fun getDatabaseKey(): String = Secrets.SQLCIPHER_PASSPHRASE
}
