package com.hatem.noureddine.bank.domain.repository

import com.hatem.noureddine.bank.domain.model.AppMode

interface DataSourceSwitcher {
    /**
     * Switches the DataSource implementation between Remote and Mock modes at runtime.
     * Unloads the old module and loads the new one based on [AppMode].
     *
     * @param mode The target [AppMode].
     */
    fun switch(mode: AppMode)
}
