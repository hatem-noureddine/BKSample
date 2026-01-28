package com.hatem.noureddine.bank.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing a Bank.
 *
 * @property name The name of the bank.
 * @property isCA Integer flag (1 or 0) indicating if the bank is Credit Agricole.
 * @property accounts List of accounts held at this bank.
 */
@Serializable
data class BankDto(
    @SerialName("name") val name: String,
    @SerialName("isCA") val isCA: Int,
    @SerialName("accounts") val accounts: List<AccountDto>,
)
