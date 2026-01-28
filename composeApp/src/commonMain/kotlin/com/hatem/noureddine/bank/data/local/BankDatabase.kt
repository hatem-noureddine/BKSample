package com.hatem.noureddine.bank.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.hatem.noureddine.bank.data.local.dao.BankDao
import com.hatem.noureddine.bank.data.local.entity.AccountEntity
import com.hatem.noureddine.bank.data.local.entity.BankEntity
import com.hatem.noureddine.bank.data.local.entity.OperationEntity

/**
 * Room Database definition for the application.
 * Contains tables for [BankEntity], [AccountEntity], and [OperationEntity].
 */
@Database(
    entities = [BankEntity::class, AccountEntity::class, OperationEntity::class],
    version = 1,
    exportSchema = false,
)
@ConstructedBy(BankDatabaseConstructor::class)
abstract class BankDatabase : RoomDatabase() {
    abstract fun bankDao(): BankDao
}
