package com.riju.drivertracker.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.riju.drivertracker.repository.LocationRepository
import com.riju.drivertracker.repository.PermissionRepository
import com.riju.drivertracker.repository.TrackingRepository
import com.riju.drivertracker.repository.model.UserPermissionState
import com.riju.drivertracker.service.model.TrackingPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.math.RoundingMode
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var trackingRepository: TrackingRepository

    @Inject
    lateinit var permissionRepository: PermissionRepository

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_TRIP_START -> start()
            ACTION_TRIP_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking")
            .setContentText("Driver is on the way")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        locationRepository.getLocationUpdates(LOCATION_UPDATE_DELAY)
            .onStart {
                notificationManager.notify(1, notification.build())
                trackingRepository.startTracking()
            }
            .onEach { locationState ->
                when (locationState) {
                    is UserPermissionState.Granted -> {
                        trackingRepository.addRoutePoint(
                            TrackingPoint(
                                lat = locationState.value.latitude,
                                lon = locationState.value.longitude,
                                speed = (locationState.value.speed * 3.6).toBigDecimal()
                                    .setScale(2, RoundingMode.HALF_UP).toDouble()
                            )
                        )
                    }

                    is UserPermissionState.Denied -> {
                        // TODO notify user about location permission denied
                        stop()
                    }
                }
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build(), FOREGROUND_SERVICE_TYPE_LOCATION)
    }

    private fun stop() {
        trackingRepository.stopTracking()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_TRIP_START = "ACTION_TRIP_START"
        const val ACTION_TRIP_STOP = "ACTION_TRIP_STOP"
        const val LOCATION_UPDATE_DELAY = 2000L
    }
}
