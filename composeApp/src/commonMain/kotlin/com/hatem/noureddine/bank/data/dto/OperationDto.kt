package com.hatem.noureddine.bank.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing a generic banking operation.
 *
 * @property id Unique identifier for the operation.
 * @property title The description or title of the operation.
 * @property amount The amount as a string (may need parsing).
 * @property category The category of the expense/income.
 * @property date The date of the operation as a string (timestamp).
 */
@Serializable
data class OperationDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("amount") val amount: String,
    @SerialName("category") val category: String,
    @SerialName("date") val date: String,
)
