package com.riju.drivertracker.ui.currenttrip

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import kotlinx.coroutines.launch

@Composable
fun CurrentTripScreen(viewModel: CurrentTripViewModel) {
    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()
    val currentTripRoute by viewModel.currentTripRoute.collectAsStateWithLifecycle()

    DTScaffold(viewModel = viewModel) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                if (currentTripRoute.isNotEmpty()) {
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLng(currentTripRoute.last()),
                        )
                    }
                }
            },
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(
                tiltGesturesEnabled = false,
            )
        ) {
            if (currentTripRoute.isNotEmpty()) {
                Polyline(
                    points = currentTripRoute,
                    clickable = true,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
