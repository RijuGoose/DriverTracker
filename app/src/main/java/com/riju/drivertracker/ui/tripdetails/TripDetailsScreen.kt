package com.riju.drivertracker.ui.tripdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.StrokeStyle
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.riju.drivertracker.R
import com.riju.drivertracker.extensions.toLocalDateString
import com.riju.drivertracker.extensions.toTimeString
import com.riju.drivertracker.ui.tripdetails.components.TripDetailCard
import com.riju.drivertracker.ui.uicomponents.DTBottomSheetScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar
import java.time.LocalDateTime

@Suppress("MagicNumber")
@Composable
fun TripDetailsScreen(
    viewModel: TripDetailsViewModel,
    onBackPressed: (() -> Unit)?
) {
    val tripPoints by viewModel.tripPoints.collectAsStateWithLifecycle()
    val mapBounds by viewModel.mapBounds.collectAsStateWithLifecycle()

    @Suppress("MagicNumber")
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapBounds.center, 10f) // TODO ezt amúgy máshogy kéne
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
    ) { uiModel ->
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(0.5f)) {
                GoogleMap(
                    cameraPositionState = cameraPositionState,
                    onMapLoaded = {
                        cameraPositionState.move(
                            update = CameraUpdateFactory.newLatLngBounds(
                                mapBounds,
                                TripDetailsViewModel.CAMERA_PADDING
                            )
                        )
                    },
                    uiSettings = MapUiSettings(
                        tiltGesturesEnabled = false
                    )
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

@Composable
fun TripDetailData(
    modifier: Modifier = Modifier,
    avgSpeed: Double,
    maxSpeed: Double,
    distance: Double,
    startTime: LocalDateTime,
    endTime: LocalDateTime?,
    startLocation: String?,
    endLocation: String?,
    getStartLocation: (() -> Unit)?,
    getEndLocation: (() -> Unit)?
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TripDetailCard(
                modifier = Modifier.weight(1f),
                cardTitle = stringResource(R.string.trip_details_avg_speed),
                cardValue = avgSpeed.toString()
            )
            TripDetailCard(
                modifier = Modifier.weight(1f),
                cardTitle = stringResource(R.string.trip_details_max_speed),
                cardValue = maxSpeed.toString()
            )
        }
        TripDetailCard(
            modifier = Modifier.fillMaxWidth(),
            cardTitle = stringResource(R.string.trip_details_distance),
            cardValue = distance.toString()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TripDetailCard(
                modifier = Modifier.weight(1f),
                cardTitle = stringResource(R.string.trip_details_start_time),
                cardValue = startTime.toLocalDateString() + "\n" + startTime.toTimeString()
            )
            TripDetailCard(
                modifier = Modifier.weight(1f),
                cardTitle = stringResource(R.string.trip_details_end_time),
                cardValue = endTime?.let {
                    it.toLocalDateString() + "\n" + it.toTimeString()
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TripDetailCard(
                modifier = Modifier.weight(1f),
                cardTitle = stringResource(R.string.trip_details_start_location),
                cardValue = startLocation,
                onClick = getStartLocation
            )
            TripDetailCard(
                modifier = Modifier.weight(1f),
                cardTitle = stringResource(R.string.trip_details_end_location),
                cardValue = endLocation,
                onClick = getEndLocation
            )
        }
    }
}
