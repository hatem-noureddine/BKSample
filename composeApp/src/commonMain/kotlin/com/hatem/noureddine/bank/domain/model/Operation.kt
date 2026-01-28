package com.hatem.noureddine.bank.domain.model

/**
 * Represents a single banking operation (transaction).
 *
 * @property id Unique identifier for the operation.
 * @property title The title or description of the operation (e.g., "Amazon", "Salary").
 * @property amount The amount of the transaction. Negative for debits, positive for credits.
 * @property category The category of the operation (e.g., "Food", "Leisure").
 * @property date The timestamp of the operation in epoch seconds.
 */
data class Operation(
    val id: String,
    val title: String,
    val amount: Double,
    val category: String,
    val date: Long, // Unix timestamp in seconds
)
