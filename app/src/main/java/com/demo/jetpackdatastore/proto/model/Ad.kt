package com.demo.jetpackdatastore.proto.model

import java.io.Serializable

data class Ad(
    val title: String,
    val type: AdType,
    val category: AdCategory
) : Serializable