package com.riju.repositoryimpl.mapper

import com.riju.localdatasourceimpl.model.SettingsDataStoreModel
import com.riju.repository.model.Settings

fun SettingsDataStoreModel.toSettings(): Settings {
    return Settings(
        automaticTrip = automaticTrip,
        shouldMergeTrips = shouldMergeTrips,
        btDeviceName = btDeviceName,
        btDeviceMacAddress = btDeviceMacAddress,
        mergeTripSeconds = mergeTripSeconds
    )
}

fun Settings.toSettingsDataStoreModel(): SettingsDataStoreModel {
    return SettingsDataStoreModel(
        automaticTrip = automaticTrip,
        shouldMergeTrips = shouldMergeTrips,
        btDeviceName = btDeviceName,
        btDeviceMacAddress = btDeviceMacAddress,
        mergeTripSeconds = mergeTripSeconds
    )
}
