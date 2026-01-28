package com.hatem.noureddine.bank.utlis

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Retrieves the path to the application's document directory on iOS.
 * Used for storing persistent data like databases and preferences.
 *
 * @return The absolute path to the document directory.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun documentDirectory(): String {
    val url: NSURL? =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
    return requireNotNull(url?.path)
}
