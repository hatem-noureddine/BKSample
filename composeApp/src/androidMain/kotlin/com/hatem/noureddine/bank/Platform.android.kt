@file:Suppress("MatchingDeclarationName")
package com.hatem.noureddine.bank

import android.os.Build

/**
 * Android-specific implementation of [Platform].
 */
class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

/**
 * Retrieves the Android platform instance.
 */
actual fun getPlatform(): Platform = AndroidPlatform()
