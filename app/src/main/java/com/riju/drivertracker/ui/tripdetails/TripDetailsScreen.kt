package com.riju.drivertracker.ui.tripdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.riju.drivertracker.ui.uicomponents.DTScaffold

@Composable
fun TripDetailsScreen(
    viewModel: TripDetailsViewModel
) {
    val tripPoints by viewModel.tripPoints.collectAsStateWithLifecycle()
    val mapBounds by viewModel.mapBounds.collectAsStateWithLifecycle()

    @Suppress("MagicNumber")
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapBounds.center, 10f) // TODO ezt amúgy máshogy kéne
    }

    var isMapLoaded by remember { mutableStateOf(false) }

    DTScaffold(
        viewModel = viewModel,
        horizontalPadding = 0.dp
    ) { uiModel ->
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Avg speed: ${uiModel.avgSpeed}")
                Text("Max speed: ${uiModel.maxSpeed}")
                Text("Distance: ${uiModel.distance}")
                Text("Start time: ${uiModel.startTime}")
                Text("End time: ${uiModel.endTime}")
                Text("Start location: ${uiModel.startLocation}")
                Text("End location: ${uiModel.endLocation}")
            }
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    cameraPositionState = cameraPositionState,
                    onMapLoaded = {
                        cameraPositionState.move(
                            update = CameraUpdateFactory.newLatLngBounds(
                                mapBounds,
                                TripDetailsViewModel.CAMERA_PADDING
                            )
                        )
                        isMapLoaded = true
                    },
                    uiSettings = MapUiSettings(
                        tiltGesturesEnabled = false
                    )
                ) {
                    if (tripPoints.isNotEmpty()) {
                        Polyline(
                            points = tripPoints.map { it.first },
                            clickable = true,
                            spans = tripPoints.map {
                                StyleSpan(it.second.toArgb())
                            }
                        )
                    }
                }
            }
        }
    }
}
