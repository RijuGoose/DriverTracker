package com.riju.drivertracker.ui.triphistory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.extensions.toLocalDateString
import com.riju.drivertracker.extensions.toTimeString
import com.riju.drivertracker.ui.theme.DriverTrackerTheme
import com.riju.drivertracker.ui.triphistory.model.TripHistoryItemUIModel
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun TripHistoryItem(
    onTripSelected: (String) -> Unit,
    trip: TripHistoryItemUIModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onTripSelected(trip.tripId) }
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = trip.startTime.toLocalDateString(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = trip.startTime.toTimeString(),
                    fontWeight = FontWeight.Bold
                )
            }
            trip.endTime?.let { endTime ->
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = endTime.toLocalDateString(), fontWeight = FontWeight.Bold)
                    Text(text = endTime.toTimeString(), fontWeight = FontWeight.Bold)
                }
            }
        }

        Text(text = "From: " + trip.startLocation.orEmpty())
        Text(text = "To: " + trip.endLocation.orEmpty())
    }
}

@Suppress("MagicNumber")
@Preview(showBackground = true)
@Composable
fun TripHistoryItemPreview() {
    DriverTrackerTheme {
        TripHistoryItem(
            onTripSelected = {},
            trip = TripHistoryItemUIModel(
                tripId = "1",
                startTime = ZonedDateTime.of(2021, 9, 2, 12, 0, 0, 0, ZoneId.of("UTC")),
                endTime = ZonedDateTime.of(2021, 9, 3, 13, 0, 0, 0, ZoneId.of("UTC")),
                startLocation = "Budapest",
                endLocation = null,
            )
        )
    }
}
