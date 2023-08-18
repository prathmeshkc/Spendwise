package com.pcandroiddev.expensemanager.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "TOKEN_DATASTORE")
//    private val Context.filterDataStore: DataStore<Preferences> by preferencesDataStore(name = "FILTER_DATASTORE")

    companion object {
        val TOKEN_KEY = stringPreferencesKey(name = "TOKEN")
        val EMAIL = stringPreferencesKey(name = "EMAIL")
        val PASSWORD = stringPreferencesKey(name = "PASSWORD")
        val EMAIL_VERIFICATION_STATUS = booleanPreferencesKey(name = "EMAIL_VERIFICATION_STATUS")
//        val SELECTED_FILTER = stringPreferencesKey(name = "SELECTED_FILTER")
    }

    /*suspend fun saveSelectedFilter(filter: String) {
        context.filterDataStore.edit {
            it[SELECTED_FILTER] = filter
        }
    }

    suspend fun getSelectedFilter(): String? {
        val preferences = context.filterDataStore.data.first()
        return preferences[SELECTED_FILTER]
    }

    suspend fun deleteSelectedFilter() {
        context.filterDataStore.edit { preferences ->
            preferences.remove(SELECTED_FILTER)
        }
    }*/

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

    suspend fun saveEmailVerificationStatus(isVerified: Boolean) {
        context.tokenDataStore.edit {
            it[EMAIL_VERIFICATION_STATUS] = isVerified
        }
    }

    suspend fun getToken(): String? {
        val preferences = context.tokenDataStore.data.first()
        return preferences[TOKEN_KEY]
    }

    suspend fun getEmailVerificationStatus(): Boolean {
        val preferences = context.tokenDataStore.data.first()
        return preferences[EMAIL_VERIFICATION_STATUS] ?: false
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
