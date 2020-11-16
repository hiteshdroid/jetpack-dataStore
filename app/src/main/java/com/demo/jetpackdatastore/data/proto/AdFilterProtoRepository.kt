package com.demo.jetpackdatastore.data.proto

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import com.demo.jetpackdatastore.AdFilterPreference
import com.demo.jetpackdatastore.data.proto.model.AdCategory
import com.demo.jetpackdatastore.data.proto.model.AdFilter
import com.demo.jetpackdatastore.data.proto.model.AdType
import com.demo.jetpackdatastore.data.proto.serializer.AdFilterPreferenceSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AdFilterProtoRepository(context: Context) {

    // #1 Creating a DataStore instance with filename and serializer
    private val dataStore: DataStore<AdFilterPreference> =
        context.createDataStore(
            fileName = "ad_list_prefs.pb",
            serializer = AdFilterPreferenceSerializer()
        )

    // #2 Saving Value to Proto DataStore
    suspend fun updateAdType(type: AdType?) {
        val adType = when (type) {
            AdType.FREE -> AdFilterPreference.AdType.FREE
            AdType.PAID -> AdFilterPreference.AdType.PAID
            else -> AdFilterPreference.AdType.TYPE_ALL
        }

        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setAdType(adType)
                .build()
        }
    }

    suspend fun updateAdCategory(category: AdCategory?) {
        val adCategory = when (category) {
            AdCategory.AUTOS -> AdFilterPreference.AdCategory.AUTOS
            AdCategory.ELECTRONICS -> AdFilterPreference.AdCategory.ELECTRONICS
            else -> AdFilterPreference.AdCategory.CATEGORY_ALL
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
                    AdFilterPreference.AdType.FREE -> AdType.FREE
                    AdFilterPreference.AdType.PAID -> AdType.PAID
                    else -> AdType.ALL
                }

                val category = when (it.adCategory) {
                    AdFilterPreference.AdCategory.AUTOS -> AdCategory.AUTOS
                    AdFilterPreference.AdCategory.ELECTRONICS -> AdCategory.ELECTRONICS
                    else -> AdCategory.ALL
                }

                AdFilter(category, type)
            }
    }
}