package com.demo.jetpackdatastore.data.list

import com.demo.jetpackdatastore.data.proto.model.Ad
import com.demo.jetpackdatastore.data.proto.model.AdCategory
import com.demo.jetpackdatastore.data.proto.model.AdFilter
import com.demo.jetpackdatastore.data.proto.model.AdType

class AdListRepository {
    fun getFilteredList(filter: AdFilter): MutableList<Ad> {
        return sampleList.filter {
            if (filter.adCategory == AdCategory.ALL && filter.adType == AdType.ALL) {
                true
            } else if (filter.adCategory == AdCategory.ALL) {
                it.type == filter.adType
            } else if (filter.adType == AdType.ALL) {
                it.category == filter.adCategory
            } else {
                it.category == filter.adCategory && it.type == filter.adType
            }
        }.toMutableList()
    }

    val types =
        arrayOf(AdType.ALL, AdType.FREE, AdType.PAID)
    val categories =
        arrayOf(AdCategory.ALL, AdCategory.AUTOS, AdCategory.ELECTRONICS)

    val sampleList =
        arrayOf(
            Ad("One", AdType.FREE, AdCategory.AUTOS),
            Ad("Two", AdType.PAID, AdCategory.ELECTRONICS),
            Ad("Three", AdType.FREE, AdCategory.ELECTRONICS),
            Ad("Four", AdType.PAID, AdCategory.ELECTRONICS),
            Ad("Five", AdType.FREE, AdCategory.AUTOS),
            Ad("Six", AdType.PAID, AdCategory.AUTOS),
            Ad("Seven", AdType.PAID, AdCategory.ELECTRONICS),
            Ad("Eight", AdType.FREE, AdCategory.AUTOS),
            Ad("Nine", AdType.PAID, AdCategory.ELECTRONICS),
            Ad("Ten", AdType.FREE, AdCategory.ELECTRONICS)
        )
}