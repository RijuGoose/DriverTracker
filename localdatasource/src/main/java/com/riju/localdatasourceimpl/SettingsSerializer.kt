package com.riju.localdatasourceimpl

import androidx.datastore.core.Serializer
import com.riju.localdatasourceimpl.model.SettingsDataStoreModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<SettingsDataStoreModel> {
    override val defaultValue: SettingsDataStoreModel
        get() = SettingsDataStoreModel()

    override suspend fun readFrom(input: InputStream): SettingsDataStoreModel =
        Json.decodeFromString(input.readBytes().decodeToString())

    override suspend fun writeTo(t: SettingsDataStoreModel, output: OutputStream) =
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = SettingsDataStoreModel.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
}
