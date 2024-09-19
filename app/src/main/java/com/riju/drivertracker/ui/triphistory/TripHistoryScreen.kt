package com.riju.drivertracker.ui.triphistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.ui.DTScaffold

@Composable
fun TripHistoryScreen(
    viewModel: TripHistoryViewModel,
    onTripSelected: (String) -> Unit
) {
    val tripHistoryList by viewModel.tripHistory.collectAsStateWithLifecycle()
    DTScaffold(viewModel = viewModel) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(onClick = viewModel::getTripHistoryByTripName) {
                Text("Order by trip name")
            }
            Button(onClick = viewModel::getTripHistoryByStartTime) {
                Text("Order by start time")
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tripHistoryList) { trip ->
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onTripSelected(trip.tripId) }
                        ) {
                            Text(text = trip.tripName)
                            Text(text = "Start time:")
                            Text(text = trip.startTime)
                        }
                    }
                }
            }
        }
    }
}
