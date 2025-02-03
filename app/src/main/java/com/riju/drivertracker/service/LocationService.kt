package com.riju.drivertracker.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.Location
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.riju.drivertracker.extensions.roundToDecimalPlaces
import com.riju.repository.DebugLogRepository
import com.riju.repository.LocationRepository
import com.riju.repository.PermissionRepository
import com.riju.repository.SettingsRepository
import com.riju.repository.TrackingRepository
import com.riju.repository.TripHistoryRepository
import com.riju.repository.model.TrackingPoint
import com.riju.repository.model.UserPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZonedDateTime
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var trackingRepository: TrackingRepository

    @Inject
    lateinit var tripHistoryRepository: TripHistoryRepository

    @Inject
    lateinit var permissionRepository: PermissionRepository

    @Inject
    lateinit var debugLogRepository: DebugLogRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            when (intent?.action) {
                ACTION_TRIP_START -> start()
                ACTION_TRIP_STOP -> stop()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private suspend fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking")
            .setContentText("Driver is on the way")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var lastLocation: Location? = null

        locationRepository.getLocationUpdates(LOCATION_UPDATE_DELAY)
            .onStart {
                notificationManager.notify(1, notification.build())

                tripHistoryRepository.getLastTripDetails()?.let { nonNullLastTrip ->
                    val duration = Duration.between(nonNullLastTrip.endTime, ZonedDateTime.now())
                    val settings = settingsRepository.settings.first()
                    val tripMergeDuration = settings.mergeTripSeconds.toLong()

                    if (settings.shouldMergeTrips && duration < Duration.ofSeconds(tripMergeDuration)) {
                        trackingRepository.resumeTrip(nonNullLastTrip)
                    } else {
                        trackingRepository.startTrip()
                    }
                } ?: trackingRepository.startTrip()

                debugLogRepository.addLog("tracking started")
            }
            .map { locationState ->
                when (locationState) {
                    is UserPermissionState.Granted -> {
                        locationState.value
                    }

                    is UserPermissionState.Denied -> {
                        debugLogRepository.addLog(
                            "denied received in LocationService (location stop, give last location)"
                        )
                        stop()
                        lastLocation
                    }
                }
            }
            .filterNotNull()
            .filter { location ->
                val nonNullLastLocation = lastLocation ?: return@filter true
                location.distanceTo(nonNullLastLocation) >= MIN_DISTANCE_BETWEEN_UPDATES
            }
            .onEach { location ->
                lastLocation = location
                trackingRepository.addRoutePoint(
                    TrackingPoint(
                        lat = location.latitude,
                        lon = location.longitude,
                        speed = (location.speed * 3.6).roundToDecimalPlaces(2)
                    )
                )
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build(), FOREGROUND_SERVICE_TYPE_LOCATION)
    }

    private suspend fun stop() {
        trackingRepository.getCurrentTripFlow().firstOrNull()?.let { tripPoints ->
            if (tripPoints.isNotEmpty()) {
                val addresses =
                    listOf(tripPoints.first(), tripPoints.last())
                        .map {
                            locationRepository.getLocationAddress(it.lat, it.lon) ?: "-"
                        }
                trackingRepository.setEndpoints(addresses[0], addresses[1])
            }
        }

        trackingRepository.stopTrip()
        debugLogRepository.addLog("endpoints set in LocationService")
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
        const val MIN_DISTANCE_BETWEEN_UPDATES = 5
    }
}
