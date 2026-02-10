package com.hatem.noureddine.bank.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

const val DATA_STORE_FILE_NAME = "settings.preferences_pb"

/**
 *   Gets the singleton DataStore instance, creating it if necessary.
 */
fun createDataStore(path: String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { path.toPath() },
    )

/**
 * Platform-agnostic factory for creating a [DataStore] instance.
 * Expect/Actual mechanism allows platform-specific file path resolution.
 */
fun interface DataStoreFactory {
    /**
     * Builds and returns the [DataStore] instance.
     */
    fun build(): DataStore<Preferences>
}

expect fun getDataStoreFactory(): DataStoreFactory
