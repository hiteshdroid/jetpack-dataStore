package com.demo.jetpackdatastore.proto

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import com.demo.jetpackdatastore.Filter
import com.demo.jetpackdatastore.proto.model.AdCategory
import com.demo.jetpackdatastore.proto.model.AdFilter
import com.demo.jetpackdatastore.proto.model.AdType
import com.demo.jetpackdatastore.proto.serializer.AdFilterPreferenceSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class AdFilterUtils(context: Context) {

    // #1 Creating a DataStore instance with filename and serializer
    private val dataStore: DataStore<Filter.AdFilterPreference> =
        context.createDataStore(
            fileName = "ad_list_prefs.pb",
            serializer = AdFilterPreferenceSerializer()
        )

    // #2 Saving Value to Proto DataStore
    suspend fun updateAdType(type: AdType?) {
        val adType = when (type) {
            AdType.FREE -> Filter.AdFilterPreference.AdType.FREE
            AdType.PAID -> Filter.AdFilterPreference.AdType.PAID
            else -> Filter.AdFilterPreference.AdType.TYPE_ALL
        }

        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setAdType(adType)
                .build()
        }
    }

    suspend fun updateAdCategory(category: AdCategory?) {
        val adCategory = when (category) {
            AdCategory.AUTOS -> Filter.AdFilterPreference.AdCategory.AUTOS
            AdCategory.ELECTRONICS -> Filter.AdFilterPreference.AdCategory.ELECTRONICS
            else -> Filter.AdFilterPreference.AdCategory.CATEGORY_ALL
        }

        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setAdCategory(adCategory)
                .build()
        }
    }

    // #3 Reading Value back from Proto DataStore
    fun getAdFilter(): Flow<AdFilter> {
        return dataStore.data
            .catch {
                AdFilter(AdCategory.ALL, AdType.ALL)
            }
            .map {
                val type = when (it.adType) {
                    Filter.AdFilterPreference.AdType.FREE -> AdType.FREE
                    Filter.AdFilterPreference.AdType.PAID -> AdType.PAID
                    else -> AdType.ALL
                }

                val category = when (it.adCategory) {
                    Filter.AdFilterPreference.AdCategory.AUTOS -> AdCategory.AUTOS
                    Filter.AdFilterPreference.AdCategory.ELECTRONICS -> AdCategory.ELECTRONICS
                    else -> AdCategory.ALL
                }

                AdFilter(category, type)
            }
    }
}