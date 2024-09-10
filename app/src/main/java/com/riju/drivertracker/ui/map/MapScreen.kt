package com.riju.drivertracker.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.maps.android.compose.GoogleMap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.StrokeStyle
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Polyline

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
    modifier: Modifier = Modifier
) {
    val locations by viewModel.locations.collectAsStateWithLifecycle()
    val location by viewModel.location.collectAsStateWithLifecycle()

    val styleSpanList = remember { listOf(styleSpan) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Button(onClick = viewModel::startLocationService) {
            Text(text = "Start")
        }
        Button(onClick = viewModel::stopLocationService) {
            Text(text = "Stop")
        }
        GoogleMap(
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
            if(locations.isNotEmpty()) {
                Polyline(
                    points = locations,
                    spans = styleSpanList,
                )
            }
        }
    }
}
