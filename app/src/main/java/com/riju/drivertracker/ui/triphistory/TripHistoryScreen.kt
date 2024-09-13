package com.riju.drivertracker.ui.triphistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

@Composable
fun TripHistoryScreen(
    viewModel: TripHistoryViewModel,
    onTripSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(viewModel.tripHistory.value) { trip ->
            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable { onTripSelected(trip.tripId) }) {
                Text(text = trip.tripName)
                Text(text = "Start time:")
                Text(text = trip.startTime)
            }
        }

    }
}