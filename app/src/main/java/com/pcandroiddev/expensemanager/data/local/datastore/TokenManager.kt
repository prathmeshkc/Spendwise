package com.pcandroiddev.expensemanager.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "TOKEN_DATASTORE")

    companion object {
        val TOKEN_KEY = stringPreferencesKey(name = "TOKEN")
        val EMAIL = stringPreferencesKey(name = "EMAIL")
        val PASSWORD = stringPreferencesKey(name = "PASSWORD")
    }

    suspend fun saveToken(token: String) {
        context.tokenDataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    suspend fun saveEmail(email: String) {
        context.tokenDataStore.edit {
            it[EMAIL] = email
        }
    }

    suspend fun savePassword(password: String) {
        context.tokenDataStore.edit {
            it[PASSWORD] = password
        }
    }

    suspend fun getToken(): String? {
        val preferences = context.tokenDataStore.data.first()
        return preferences[TOKEN_KEY]
    }

    suspend fun getEmail(): String? {
        val preferences = context.tokenDataStore.data.first()
        return preferences[EMAIL]
    }

    suspend fun getPassword(): String? {
        val preferences = context.tokenDataStore.data.first()
        return preferences[PASSWORD]
    }

    suspend fun deleteToken() {
        context.tokenDataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    suspend fun deleteEmail() {
        context.tokenDataStore.edit { preferences ->
            preferences.remove(EMAIL)
        }
    }

    suspend fun deletePassword() {
        context.tokenDataStore.edit { preferences ->
            preferences.remove(PASSWORD)
        }
    }
}
