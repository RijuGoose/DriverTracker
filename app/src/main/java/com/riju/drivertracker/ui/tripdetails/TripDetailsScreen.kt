package com.riju.drivertracker.ui.tripdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.StrokeStyle
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.tripdetails.components.TripDetailData
import com.riju.drivertracker.ui.uicomponents.DTBottomSheetScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar

@Suppress("MagicNumber")
@Composable
fun TripDetailsScreen(
    viewModel: TripDetailsViewModel,
    onBackPressed: (() -> Unit)?
) {
    val tripPoints by viewModel.tripPoints.collectAsStateWithLifecycle()
    val mapBounds by viewModel.mapBounds.collectAsStateWithLifecycle()

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(mapBounds) {
        mapBounds?.let { bounds ->
            cameraPositionState.move(
                update = CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    TripDetailsViewModel.CAMERA_PADDING
                )
            )
        }
    }

    DTBottomSheetScaffold(
        viewModel = viewModel,
        sheetContent = { uiModel ->
            TripDetailData(
                avgSpeed = uiModel.avgSpeed,
                maxSpeed = uiModel.maxSpeed,
                distance = uiModel.distance,
                startTime = uiModel.startTime,
                endTime = uiModel.endTime,
                startLocation = uiModel.startLocation,
                endLocation = uiModel.endLocation,
                getStartLocation = viewModel::getStartLocation,
                getEndLocation = viewModel::getEndLocation
            )
        },
        horizontalPadding = 0.dp,
        topBar = DTTopAppBar(
            title = stringResource(R.string.trip_details_top_bar_title),
            onBackButtonClicked = onBackPressed
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(0.5f)) {
                GoogleMap(
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        tiltGesturesEnabled = false
                    ),
                    contentPadding = PaddingValues(bottom = 64.dp)
                ) {
                    if (tripPoints.isNotEmpty()) {
                        Polyline(
                            points = tripPoints.map { it.first },
                            clickable = true,
                            spans = List(tripPoints.size) { index ->
                                val nextIndex = if (index == tripPoints.size - 1) index else index + 1
                                StyleSpan(
                                    StrokeStyle.gradientBuilder(
                                        tripPoints[index].second.toArgb(),
                                        tripPoints[nextIndex].second.toArgb()
                                    ).build()
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
