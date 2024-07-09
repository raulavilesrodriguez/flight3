package com.example.flight3.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val USER_TEXTS = stringSetPreferencesKey("user_text")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveUserText(text:String){
        dataStore.edit {preferences ->
            val currentTexts = preferences[USER_TEXTS]?.toMutableSet() ?: mutableSetOf()
            currentTexts.add(text)
            preferences[USER_TEXTS] = currentTexts
        }
    }

    val userTexts: Flow<Set<String>> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USER_TEXTS] ?: emptySet()
        }
}