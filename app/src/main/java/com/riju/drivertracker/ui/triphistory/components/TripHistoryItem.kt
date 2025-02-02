package com.riju.drivertracker.ui.triphistory.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { onTripSelected(trip.tripId) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trip.startTime.toLocalDateString(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = trip.startTime.toTimeString(),
                    fontWeight = FontWeight.Bold
                )
                Text(text = trip.startLocation.orEmpty())
            }
            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            trip.endTime?.let { endTime ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = endTime.toLocalDateString(), fontWeight = FontWeight.Bold)
                    Text(text = endTime.toTimeString(), fontWeight = FontWeight.Bold)
                    Text(
                        text = trip.endLocation.orEmpty(),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
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
