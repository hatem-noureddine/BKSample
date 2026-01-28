package com.hatem.noureddine.bank.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Database Entity representing an Account.
 *
 * @property id Unique identifier for the account (PrimaryKey).
 * @property bankName Foreign key referencing the parent Bank (indexed).
 * @property order Display order.
 * @property holder Account holder name.
 * @property role User role.
 * @property contractNumber Account contract number.
 * @property label Account label.
 * @property productCode Account product code.
 * @property balance Current balance.
 */
@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = BankEntity::class,
            parentColumns = ["name"],
            childColumns = ["bankName"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [androidx.room.Index(value = ["bankName"])],
)
data class AccountEntity(
    @PrimaryKey val id: String,
    val bankName: String,
    val order: Int,
    val holder: String,
    val role: Int,
    val contractNumber: String,
    val label: String,
    val productCode: String,
    val balance: Double,
)
