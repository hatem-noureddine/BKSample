package com.hatem.noureddine.bank.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Database Entity representing an Operation (Transaction).
 *
 * @property id Unique identifier for the operation (PrimaryKey).
 * @property accountId Foreign key referencing the parent Account (indexed).
 * @property title Operation description.
 * @property amount Operation amount.
 * @property category Operation category.
 * @property date Timestamp of the operation (epoch seconds).
 */
@Entity(
    tableName = "operations",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [androidx.room.Index(value = ["accountId"])],
)
data class OperationEntity(
    @PrimaryKey val id: String,
    val accountId: String,
    val title: String,
    val amount: Double,
    val category: String,
    val date: Long,
)
