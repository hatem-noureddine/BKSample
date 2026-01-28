package com.hatem.noureddine.bank.domain.model

/**
 * Represents a bank account.
 *
 * @property id Unique identifier for the account.
 * @property order The display order of the account.
 * @property holder The name of the account holder.
 * @property role The role associated with the account (e.g., owner, proxy).
 * @property contractNumber The contract number of the account.
 * @property label The human-readable label or name of the account.
 * @property productCode The internal product code for the account type.
 * @property balance The current balance of the account.
 * @property operations The list of operations (transactions) associated with this account.
 */
data class Account(
    val id: String,
    val order: Int,
    val holder: String,
    val role: Int,
    val contractNumber: String,
    val label: String,
    val productCode: String,
    val balance: Double,
    val operations: List<Operation>,
)
