package com.riju.drivertracker.ui.triphistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.triphistory.components.TripHistoryItem
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar

@Composable
fun TripHistoryScreen(
    viewModel: TripHistoryViewModel,
    onTripSelected: (String) -> Unit
) {
    DTScaffold(
        viewModel = viewModel,
        topBar = DTTopAppBar(
            title = stringResource(R.string.trip_history_top_bar_title)
        ),
        onRefresh = viewModel::getTripHistory,
    ) { tripHistoryList ->
        if (tripHistoryList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(stringResource(R.string.trip_history_no_trips_found))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tripHistoryList) { trip ->
                    TripHistoryItem(
                        onTripSelected = onTripSelected,
                        trip = trip
                    )
                }
            }
        }
    }
}
