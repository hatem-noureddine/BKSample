package com.hatem.noureddine.bank.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlin.io.path.createTempFile

actual class DataStoreFactory {
    actual fun build(): DataStore<Preferences> {
        val file = createTempFile(prefix = "banktest-", suffix = DATA_STORE_FILE_NAME).toFile()
        return createDataStore(path = file.absolutePath)
    }
}
