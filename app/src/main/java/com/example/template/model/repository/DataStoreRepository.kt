package com.example.template.model.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val EXAMPLE_KEY = intPreferencesKey("count_number")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    suspend fun setExampleData(data: Int) {
        dataStore.edit { settings ->
            settings[EXAMPLE_KEY] = data
        }
    }

    fun getExampleData(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[EXAMPLE_KEY] ?: 0

        }
    }

    suspend fun setAccessToken(token: String) {
        dataStore.edit { settings ->
            settings[ACCESS_TOKEN] = token
        }
    }

    fun getAccessToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }
    }
}