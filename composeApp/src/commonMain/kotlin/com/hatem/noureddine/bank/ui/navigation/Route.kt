package com.hatem.noureddine.bank.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for the application.
 */
sealed interface Route {
    /** Route for the Accounts List screen. */
    @Serializable
    data object Accounts : Route

    /** Route for the Settings screen. */
    @Serializable
    data object Settings : Route

    /**
     * Route for the Operations details screen.
     * @property accountId The ID of the account to show details for.
     */
    @Serializable
    data class Operations(
        val accountId: String,
    ) : Route
}
