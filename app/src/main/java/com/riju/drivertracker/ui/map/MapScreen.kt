package com.riju.drivertracker.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.riju.drivertracker.ui.DTScaffold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    navigateToTripHistory: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.onLogoutSuccess.collectLatest {
            onLogout()
        }
    }

    DTScaffold( viewModel = viewModel) {
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
        }
    }
}
