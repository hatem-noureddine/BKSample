package com.hatem.noureddine.bank.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for the API response containing a list of banks.
 *
 * @property banks List of [BankDto] objects.
 */
@Serializable
data class BankMockResponseDto(
    @SerialName("banks") val banks: List<BankDto>,
)
