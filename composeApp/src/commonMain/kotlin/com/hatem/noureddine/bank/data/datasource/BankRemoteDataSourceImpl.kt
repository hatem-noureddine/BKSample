package com.hatem.noureddine.bank.data.datasource

import com.hatem.noureddine.bank.data.dto.BankDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Remote implementation of [BankDataSource].
 * Fetches data from a configured REST API endpoint.
 *
 * @property client [HttpClient] used for making network requests.
 */
class BankRemoteDataSourceImpl(
    private val client: HttpClient,
) : BankDataSource {
    companion object {
        private const val REMOTE_URL =
            "https://cdf-test-mobile-default-rtdb.europe-west1.firebasedatabase.app/" +
                "banks.json"
    }

    /**
     * Performs a GET request to the remote URL.
     */
    override suspend fun fetchBanks(): List<BankDto> = client.get(REMOTE_URL).body()
}
