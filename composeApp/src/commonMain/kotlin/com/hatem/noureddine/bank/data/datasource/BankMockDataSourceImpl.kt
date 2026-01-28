package com.hatem.noureddine.bank.data.datasource

import banktest.composeapp.generated.resources.Res
import com.hatem.noureddine.bank.data.dto.BankDto
import com.hatem.noureddine.bank.data.dto.BankMockResponseDto
import kotlinx.serialization.json.Json

/**
 * Mock implementation of [BankDataSource].
 * Reads data from a local JSON file included in resources.
 */
class BankMockDataSourceImpl : BankDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Reads `files/bank.json` from resources and deserializes it.
     */
    override suspend fun fetchBanks(): List<BankDto> {
        val bytes = Res.readBytes("files/bank.json")
        val jsonString = bytes.decodeToString()
        val banks: BankMockResponseDto = json.decodeFromString(jsonString)
        return banks.banks
    }
}
