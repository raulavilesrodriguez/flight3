package com.example.flight3

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flight3.data.AppContainer
import com.example.flight3.data.AppDataContainer
import com.example.flight3.data.UserPreferencesRepository

private const val USER_TEXT_NAME = "user_text"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_TEXT_NAME
)

class FlightApplication : Application() {

    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}