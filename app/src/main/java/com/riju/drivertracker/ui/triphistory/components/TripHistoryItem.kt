package com.riju.drivertracker.ui.triphistory.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.extensions.toLocalDateString
import com.riju.drivertracker.extensions.toTimeString
import com.riju.drivertracker.ui.triphistory.model.TripHistoryItemUIModel
import java.time.LocalDateTime

@Composable
fun TripHistoryItem(
    onTripSelected: (String) -> Unit,
    trip: TripHistoryItemUIModel
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onTripSelected(trip.tripId) }
                .padding(8.dp)
        ) {
            if (trip.startTime.toLocalDate() == trip.endTime?.toLocalDate()) {
                Text(text = trip.startTime.toLocalDateString(), fontWeight = FontWeight.Bold)
                Text(
                    text = trip.startTime.toTimeString() + " - " + trip.endTime?.toTimeString(),
                    fontWeight = FontWeight.Bold
                )
            } else {
                Row {
                    Column {
                        Text(text = trip.startTime.toLocalDateString(), fontWeight = FontWeight.Bold)
                        Text(text = trip.startTime.toTimeString(), fontWeight = FontWeight.Bold)
                    }
                    Text(" - ")
                    trip.endTime?.let { endTime ->
                        Column {
                            Text(text = endTime.toLocalDateString(), fontWeight = FontWeight.Bold)
                            Text(text = endTime.toTimeString(), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Text(text = trip.startLocation + " - " + trip.endLocation)
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
fun TripHistoryItemPreview() {
    TripHistoryItem(
        onTripSelected = {},
        trip = TripHistoryItemUIModel(
            tripId = "1",
            startTime = LocalDateTime.of(2021, 9, 2, 12, 0),
            endTime = LocalDateTime.of(2021, 9, 3, 13, 0),
            startLocation = "Budapest",
            endLocation = null,
        )
    )
}
