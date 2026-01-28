package com.hatem.noureddine.bank.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.mp.KoinPlatform

/**
 * Android implementation for creating DataStore.
 * Resolves the absolute path for the preferences file using the application context.
 */
actual class DataStoreFactory {
    actual fun build(): DataStore<Preferences> {
        val context = KoinPlatform.getKoin().get<Context>()
        val path = context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
        return createDataStore(path = path)
    }
}
