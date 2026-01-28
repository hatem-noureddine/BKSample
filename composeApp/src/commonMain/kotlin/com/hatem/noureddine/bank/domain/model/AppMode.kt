package com.hatem.noureddine.bank.domain.model

/**
 * Represents the operating mode of the application.
 *
 * @property REMOTE application fetches data from the remote server.
 * @property MOCK application uses local mock data for testing/demo purposes.
 */
enum class AppMode {
    REMOTE,
    MOCK,
}
