package com.riju.drivertracker.ui.tripdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.StrokeStyle
import com.google.android.gms.maps.model.StyleSpan
import com.google.maps.android.compose.GoogleMap
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
fun TripDetailsScreen(
    viewModel: TripDetailsViewModel
) {
    val styleSpanList = remember { listOf(styleSpan) }

    val tripPoints by viewModel.tripPoints.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
            if (tripPoints.isNotEmpty()) {
                Polyline(
                    points = tripPoints,
                    spans = styleSpanList,
                )
            }
        }
    }
}