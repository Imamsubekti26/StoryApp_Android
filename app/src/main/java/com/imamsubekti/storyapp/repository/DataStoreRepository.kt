package com.imamsubekti.storyapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreRepository private constructor(private val dataStore: DataStore<Preferences>){
    private val theme = booleanPreferencesKey("theme_setting")
    private val lang = booleanPreferencesKey("language_setting")
    private val token = stringPreferencesKey("bearer_token")

    fun getTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[theme] ?: false
        }
    }

    suspend fun setTheme(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[theme] = isDarkModeActive
        }
    }

    fun getLang(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[lang] ?: false
        }
    }

    suspend fun setLang(isEnglish: Boolean) {
        dataStore.edit { preferences ->
            preferences[lang] = isEnglish
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[token] ?: ""
        }
    }

    suspend fun setToken(tokenString: String) {
        dataStore.edit { preferences ->
            preferences[token] = tokenString
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataStoreRepository? = null

        fun getInstance(dataStore: DataStore<Preferences>): DataStoreRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStoreRepository(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}