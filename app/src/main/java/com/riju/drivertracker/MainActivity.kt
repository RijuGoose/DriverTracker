package com.riju.drivertracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.riju.drivertracker.ui.navigation.DTNavHost
import com.riju.drivertracker.ui.theme.DriverTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleStartScreen(intent)
        createNotificationChannel()

        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()

            DriverTrackerTheme {
                DTNavHost(
                    mainViewModel = mainViewModel,
                    navHostController = navHostController,
                )
            }
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "location",
            "Location",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun handleStartScreen(intent: Intent?) {
        mainViewModel.setStartScreen(intent?.action)
    }
}
