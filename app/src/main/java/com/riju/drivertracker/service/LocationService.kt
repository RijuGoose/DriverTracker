package com.riju.drivertracker.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.riju.drivertracker.repository.LocationRepository
import com.riju.drivertracker.repository.TrackingRepository
import com.riju.drivertracker.service.model.TrackingPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
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

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Location Service")
            .setContentText("Location updates are coming")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        locationRepository.getLocationUpdates(LOCATION_UPDATE_DELAY)
            .catch { e -> e.printStackTrace() }
            .onStart {
                notificationManager.notify(1, notification.build())
                trackingRepository.startTracking()
            }
            .onEach { location ->
                trackingRepository.addRoutePoint(
                    TrackingPoint(
                        lat = location.latitude,
                        lon = location.longitude,
                        speed = (location.speed * 3.6).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
                    )
                )
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
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
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val LOCATION_UPDATE_DELAY = 2000L
    }
}
