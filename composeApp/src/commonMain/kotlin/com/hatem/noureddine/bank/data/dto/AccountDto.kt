package com.hatem.noureddine.bank.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing a Bank Account.
 *
 * @property id Unique identifier for the account.
 * @property order The order in which the account should be displayed.
 * @property holder Name of the account holder.
 * @property role Role identifier for the user's relation to the account.
 * @property contractNumber The contract number associated with the account.
 * @property label The display name of the account.
 * @property productCode The internal code representing the account type.
 * @property balance The current balance of the account.
 * @property operations List of recent operations (transactions) for this account.
 */
@Serializable
data class AccountDto(
    @SerialName("id") val id: String,
    @SerialName("order") val order: Int,
    @SerialName("holder") val holder: String,
    @SerialName("role") val role: Int,
    @SerialName("contract_number") val contractNumber: String,
    @SerialName("label") val label: String,
    @SerialName("product_code") val productCode: String,
    @SerialName("balance") val balance: Double,
    @SerialName("operations") val operations: List<OperationDto>,
)
