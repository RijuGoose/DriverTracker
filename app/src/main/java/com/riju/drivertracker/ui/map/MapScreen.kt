package com.riju.drivertracker.ui.map

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    navigateToTripHistory: () -> Unit,
    navigateToCurrentTrip: () -> Unit
) {
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
        if (!allPermissions.allPermissionsGranted) {
            allPermissions.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onLogoutSuccess.collectLatest {
            onLogout()
        }
    }

    DTScaffold(viewModel = viewModel) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Button(
                onClick = viewModel::startLocationService,
                enabled = locationPermissionState.allPermissionsGranted
            ) {
                Text(text = "Start")
            }
            Button(
                onClick = viewModel::stopLocationService,
                enabled = locationPermissionState.allPermissionsGranted
            ) {
                Text(text = "Stop")
            }
            Button(onClick = viewModel::logout) {
                Text(text = "Logout")
            }
            Button(onClick = navigateToTripHistory) {
                Text(text = "Trip History")
            }
            Button(onClick = navigateToCurrentTrip) {
                Text(text = "Current trip")
            }
        }
    }
}
