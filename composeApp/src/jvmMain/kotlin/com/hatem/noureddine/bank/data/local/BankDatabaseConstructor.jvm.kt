package com.hatem.noureddine.bank.data.local

import androidx.room.RoomDatabaseConstructor

actual object BankDatabaseConstructor : RoomDatabaseConstructor<BankDatabase> {
    actual override fun initialize(): BankDatabase =
        throw UnsupportedOperationException("BankDatabase constructor is not available on JVM tests.")
}
