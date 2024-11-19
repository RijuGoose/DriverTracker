package com.riju.drivertracker.ui.triphistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.triphistory.components.TripHistoryItem
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar
import com.riju.drivertracker.ui.uicomponents.DTTopBarActionButton
import com.riju.drivertracker.ui.uicomponents.DTTopBarActionButton.ActionIcon

@Composable
fun TripHistoryScreen(
    viewModel: TripHistoryViewModel,
    onTripSelected: (String) -> Unit
) {
    val isOrderAscending by viewModel.isOrderAscending.collectAsState()
    DTScaffold(
        viewModel = viewModel,
        horizontalPadding = 0.dp,
        topBar = DTTopAppBar(
            title = stringResource(R.string.trip_history_top_bar_title),
            actionButtons = listOf(
                DTTopBarActionButton(
                    icon = ActionIcon(
                        icon = Icons.AutoMirrored.Filled.Sort,
                        modifier = if (isOrderAscending) {
                            Modifier.scale(scaleX = 1f, scaleY = -1f)
                        } else {
                            Modifier
                        }
                    ),
                    onClick = {
                        viewModel.changeSortOrder()
                    },
                    contentDescription = "Sort"
                )
            )
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
