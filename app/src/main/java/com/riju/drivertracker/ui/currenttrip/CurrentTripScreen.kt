package com.riju.drivertracker.ui.currenttrip

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.riju.drivertracker.R
import com.riju.drivertracker.extensions.openSettings
import com.riju.drivertracker.extensions.shouldShowDialog
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar
import com.riju.drivertracker.ui.uicomponents.PermissionAlertDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CurrentTripScreen(
    viewModel: CurrentTripViewModel
) {
    val currentTripRoute by viewModel.currentTripRoute.collectAsStateWithLifecycle()
    val isTracking by viewModel.isTracking.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    val locationPermissionDialog by viewModel.locationPermissionDialog.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val allPermissions = rememberMultiplePermissionsState(
        permissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }
    )

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        if (!allPermissions.shouldShowDialog()) {
            allPermissions.launchMultiplePermissionRequest()
        }
    }

    DTScaffold(
        viewModel = viewModel,
        horizontalPadding = 0.dp,
        topBar = DTTopAppBar(
            title = stringResource(R.string.current_trip_top_bar_title)
        )
    ) {
        CurrentTripScreenBody(
            onStartClick = {
                if (locationPermissionState.allPermissionsGranted) {
                    viewModel.startLocationService()
                } else if (locationPermissionState.shouldShowDialog()) {
                    viewModel.showLocationPermissionDialog()
                } else {
                    locationPermissionState.launchMultiplePermissionRequest()
                }
            },
            onStopClick = viewModel::stopLocationService,
            isTripRunning = isTracking,
            isLocationPermissionsGranted = locationPermissionState.allPermissionsGranted,
            currentTripRoute = currentTripRoute,
            cameraPositionState = cameraPositionState,
            currentLocation = currentLocation
        )

        PermissionAlertDialog(
            showDialog = locationPermissionDialog,
            permissionName = "Location",
            onConfirmButton = {
                context.openSettings()
                viewModel.hideLocationPermissionDialog()
            },
            onDismissDialog = viewModel::hideLocationPermissionDialog
        )
    }
}

@Composable
private fun CurrentTripScreenBody(
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    isTripRunning: Boolean,
    isLocationPermissionsGranted: Boolean,
    currentTripRoute: List<LatLng>,
    cameraPositionState: CameraPositionState,
    currentLocation: LatLng?
) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = onStartClick,
                enabled = !isTripRunning,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Text(text = stringResource(R.string.current_trip_button_start))
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = onStopClick,
                enabled = isLocationPermissionsGranted && isTripRunning,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = stringResource(R.string.current_trip_button_stop))
            }
        }
        if (isLocationPermissionsGranted) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true),
                onMapLoaded = {
                    coroutineScope.launch {
                        currentLocation?.let {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(it, CurrentTripViewModel.ZOOM_LEVEL)
                            )
                        }
                    }
                },
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
}
