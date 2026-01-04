package com.slimmy.portoapps.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.configDataStore: DataStore<Preferences> by preferencesDataStore(name = "config")

class AppConfig(private val context: Context) {

    companion object {
        private val LATEST_VERSION_KEY = stringPreferencesKey("latest_version")
    }

    val latestVersion: Flow<String> = context.configDataStore.data
        .map { preferences ->
            preferences[LATEST_VERSION_KEY] ?: "-"
        }

    suspend fun saveLatestVersion(version: String) {
        context.configDataStore.edit { preferences ->
            preferences[LATEST_VERSION_KEY] = version
        }
    }
}
