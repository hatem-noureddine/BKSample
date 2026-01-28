package com.hatem.noureddine.bank.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database Entity representing a Bank.
 *
 * @property name The name of the bank (PrimaryKey).
 * @property isCA Boolean indicating if it is a Credit Agricole bank.
 */
@Entity(tableName = "banks")
data class BankEntity(
    @PrimaryKey val name: String,
    val isCA: Boolean,
)
