package com.riju.drivertracker.ui.tripdetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.R
import com.riju.drivertracker.extensions.toLocalDateString
import com.riju.drivertracker.extensions.toTimeString
import java.time.ZonedDateTime

@Composable
fun TripDetailData(
    modifier: Modifier = Modifier,
    maxSpeed: Double,
    distance: Double,
    startTime: ZonedDateTime,
    endTime: ZonedDateTime?,
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
                cardTitle = stringResource(R.string.trip_details_distance),
                cardValue = stringResource(R.string.trip_details_distance_in_km, distance)
            )
            TripDetailCard(
                modifier = Modifier.weight(1f),
                cardTitle = stringResource(R.string.trip_details_max_speed),
                cardValue = maxSpeed.toString()
            )
        }
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
