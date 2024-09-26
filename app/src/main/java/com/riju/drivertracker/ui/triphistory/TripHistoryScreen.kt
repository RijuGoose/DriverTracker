package com.riju.drivertracker.ui.triphistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar

@Composable
fun TripHistoryScreen(
    viewModel: TripHistoryViewModel,
    onTripSelected: (String) -> Unit
) {
    val tripHistoryList by viewModel.tripHistory.collectAsStateWithLifecycle()
    DTScaffold(
        viewModel = viewModel,
        topBar = DTTopAppBar(
            title = stringResource(R.string.trip_history_top_bar_title)
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (tripHistoryList.isEmpty()) {
                Text(stringResource(R.string.trip_history_no_trips_found))
            } else {
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
                                Text(text = stringResource(R.string.trip_history_start_time))
                                Text(text = trip.startTime)
                            }
                        }
                    }
                }
            }
        }
    }
}
