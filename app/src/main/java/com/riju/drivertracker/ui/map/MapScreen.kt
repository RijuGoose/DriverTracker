package com.riju.drivertracker.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.riju.drivertracker.ui.ScreenStatus
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    snackBarHostState: SnackbarHostState,
    navigateToTripHistory: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.screenStatus.collectLatest {
            when (it) {
                is ScreenStatus.Success -> onLogout()
                is ScreenStatus.Failure -> {
                    snackBarHostState.showSnackbar(it.error)
                }

                else -> {
                    /* no-op */
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Button(onClick = viewModel::startLocationService) {
            Text(text = "Start")
        }
        Button(onClick = viewModel::stopLocationService) {
            Text(text = "Stop")
        }
        Button(onClick = viewModel::logout) {
            Text(text = "Logout")
        }
        Button(onClick = navigateToTripHistory) {
            Text(text = "Trip History")
        }
        GoogleMap(
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
//            if (locations.isNotEmpty()) {
//                Polyline(
//                    points = locations,
//                    spans = styleSpanList,
//                )
//            }
        }
    }
}
