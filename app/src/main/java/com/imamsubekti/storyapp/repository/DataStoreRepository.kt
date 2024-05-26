package com.imamsubekti.storyapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreRepository private constructor(private val dataStore: DataStore<Preferences>){
    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[BEARER_TOKEN] ?: ""
        }
    }

    suspend fun setToken(tokenString: String) {
        dataStore.edit { preferences ->
            preferences[BEARER_TOKEN] = tokenString
        }
    }

    suspend fun logout() {
        dataStore.edit { preference ->
            preference.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataStoreRepository? = null

        private val BEARER_TOKEN = stringPreferencesKey("bearer_token")

        fun getInstance(dataStore: DataStore<Preferences>): DataStoreRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStoreRepository(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}