package com.hatem.noureddine.bank.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hatem.noureddine.bank.utlis.documentDirectory

/**
 * iOS implementation for creating DataStore.
 * Resolves the absolute path for the preferences file in the document directory.
 */
private class DataStoreFactoryIOS : DataStoreFactory {
    override fun build(): DataStore<Preferences> {
        val path = "${documentDirectory()}/$DATA_STORE_FILE_NAME"
        return createDataStore(path = path)
    }
}

actual fun getDataStoreFactory(): DataStoreFactory = DataStoreFactoryIOS()
