package com.demo.jetpackdatastore.data.preference

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceRepository(context: Context) {

    // #1 Creating a DataStore instance
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "app_preference"
    )

    // #2 Defining Key for the Value that needs to be saved
    private val keyAppLaunchCounter =
        preferencesKey<Int>(name = "app_launch_counter")

    // #3 Saving Value to Preference DataStore
    suspend fun incrementAppLaunchCounter() {
        dataStore.edit { preferences ->
            val currentCounterValue = preferences[keyAppLaunchCounter] ?: 0
            preferences[keyAppLaunchCounter] = currentCounterValue + 1
        }
    }

    // #4 Reading Value back from Preference DataStore
    fun getCurrentAppLaunchCounter() : Flow<Int> {
        return dataStore.data.map { preferences->
            preferences[keyAppLaunchCounter]?: 0
        }
    }

    suspend fun clearAppLaunchCounter() {
        dataStore.edit { preferences ->
            preferences.remove(keyAppLaunchCounter)
        }
    }
}