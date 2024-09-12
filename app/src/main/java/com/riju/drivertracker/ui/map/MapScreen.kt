package com.riju.drivertracker.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.maps.android.compose.GoogleMap
import com.google.android.gms.maps.model.StrokeStyle
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.compose.MapProperties
import kotlinx.coroutines.flow.collectLatest

val styleSpan = StyleSpan(
    StrokeStyle.gradientBuilder(
        Color.Red.toArgb(),
        Color.Green.toArgb(),
    )
        .build()
)

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val styleSpanList = remember { listOf(styleSpan) }

    LaunchedEffect(Unit) {
        viewModel.logoutStatus.collectLatest {
            when (it) {
                is LogoutState.Success -> onLogout()
                is LogoutState.Failure -> {
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
