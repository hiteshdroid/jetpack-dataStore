package com.demo.jetpackdatastore.data.proto.serializer

import androidx.datastore.CorruptionException
import androidx.datastore.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.demo.jetpackdatastore.AdFilterPreference
import com.demo.jetpackdatastore.Filter
import java.io.InputStream
import java.io.OutputStream

class AdFilterPreferenceSerializer : Serializer<AdFilterPreference>{
    override fun readFrom(input: InputStream): AdFilterPreference {
        try {
            return AdFilterPreference.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: AdFilterPreference, output: OutputStream) {
        t.writeTo(output)
    }
}