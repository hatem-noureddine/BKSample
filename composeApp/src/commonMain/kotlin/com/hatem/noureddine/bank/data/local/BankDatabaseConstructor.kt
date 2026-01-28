package com.hatem.noureddine.bank.data.local

import androidx.room.RoomDatabaseConstructor

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object BankDatabaseConstructor : RoomDatabaseConstructor<BankDatabase> {
    override fun initialize(): BankDatabase
}
