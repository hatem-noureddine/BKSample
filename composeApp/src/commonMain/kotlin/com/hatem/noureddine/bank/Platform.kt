package com.hatem.noureddine.bank

/**
 * Interface representing the current platform.
 *
 * @property name The name of the platform (e.g., "Android", "iOS").
 */
interface Platform {
    val name: String
}

/**
 * Retrieves the current [Platform] instance.
 * @return The platform-specific implementation.
 */
expect fun getPlatform(): Platform
