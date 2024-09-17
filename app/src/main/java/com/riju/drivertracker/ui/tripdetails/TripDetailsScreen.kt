package com.riju.drivertracker.ui.tripdetails

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.riju.drivertracker.ui.DTScaffold

@Composable
fun TripDetailsScreen(
    viewModel: TripDetailsViewModel
) {
    val tripPoints by viewModel.tripPoints.collectAsStateWithLifecycle()
    val mapBounds by viewModel.mapBounds.collectAsStateWithLifecycle()

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(mapBounds) {
        cameraPositionState.move(
            update = CameraUpdateFactory.newLatLngBounds(
                mapBounds,
                200
            )
        )
    }

    DTScaffold(viewModel = viewModel) {
        Column(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                properties = MapProperties(
                    isMyLocationEnabled = true
                ),
                cameraPositionState = cameraPositionState
            ) {
                if (tripPoints.isNotEmpty()) {
                    Polyline(
                        points = tripPoints
                    )
                }
            }
        }
    }
}