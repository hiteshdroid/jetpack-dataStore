package com.demo.jetpackdatastore.proto.serializer

import androidx.datastore.CorruptionException
import androidx.datastore.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.demo.jetpackdatastore.Filter
import java.io.InputStream
import java.io.OutputStream

class AdFilterPreferenceSerializer : Serializer<Filter.AdFilterPreference>{
    override fun readFrom(input: InputStream): Filter.AdFilterPreference {
        try {
            return Filter.AdFilterPreference.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: Filter.AdFilterPreference, output: OutputStream) {
        t.writeTo(output)
    }
}