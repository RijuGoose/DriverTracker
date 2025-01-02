package com.riju.repositoryimpl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.riju.localdatasourceimpl.LocalDebugLogDataSource
import com.riju.repository.LocationRepository
import com.riju.repository.model.UserPermissionState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val client: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val localDebugLogDataSource: LocalDebugLogDataSource
) : LocationRepository {

    override fun getLocationUpdates(interval: Long): Flow<UserPermissionState<Location>> {
        return callbackFlow {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                localDebugLogDataSource.addLog("Location permission is not granted (send denied permission state)")
                send(UserPermissionState.Denied)
                close()
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                localDebugLogDataSource.addLog("Location is not enabled (send denied permission state)")
                send(UserPermissionState.Denied)
                close()
            }

            val request = LocationRequest.Builder(interval).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.lastLocation?.let { location ->
                        launch {
                            send(UserPermissionState.Granted(location))
                        }
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    super.onLocationAvailability(locationAvailability)
                    if (!locationAvailability.isLocationAvailable) {
                        launch {
                            localDebugLogDataSource.addLog("Location is not available (send denied permission state)")
                            send(UserPermissionState.Denied)
                            close()
                        }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    override suspend fun getCurrentLocation(): UserPermissionState<Location> {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return UserPermissionState.Denied
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGpsEnabled && !isNetworkEnabled) {
            return UserPermissionState.Denied
        }

        val cancellationToken = object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                return CancellationTokenSource().token
            }

            override fun isCancellationRequested(): Boolean {
                return false
            }
        }

        return UserPermissionState.Granted(
            client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken).await()
        )
    }

    override suspend fun getLocationAddress(lat: Double, lon: Double): Address? {
        return if (Geocoder.isPresent()) {
            suspendCoroutine { continuation ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        lat,
                        lon,
                        1
                    ) { addresses ->
                        continuation.resume(addresses.firstOrNull())
                    }
                } else {
                    val addresses = geocoder.getFromLocation(
                        lat,
                        lon,
                        1
                    )
                    continuation.resume(addresses?.firstOrNull())
                }
            }
        } else {
            null
        }
    }
}
