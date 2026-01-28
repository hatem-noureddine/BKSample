package com.hatem.noureddine.bank.data.datasource

import com.hatem.noureddine.bank.data.dto.BankDto

/**
 * Interface representing a data source for fetching Bank data.
 * Can be implemented by Remote (Network) or Local (Mock) sources.
 */
interface BankDataSource {
    /**
     * Fetches the list of banks and accounts.
     * @return List of [BankDto] objects.
     */
    suspend fun fetchBanks(): List<BankDto>
}
