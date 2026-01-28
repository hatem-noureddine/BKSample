@file:Suppress("MatchingDeclarationName")
package com.hatem.noureddine.bank

import platform.UIKit.UIDevice

/**
 * iOS-specific implementation of [Platform].
 */
class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

/**
 * Retrieves the iOS platform instance.
 */
actual fun getPlatform(): Platform = IOSPlatform()
