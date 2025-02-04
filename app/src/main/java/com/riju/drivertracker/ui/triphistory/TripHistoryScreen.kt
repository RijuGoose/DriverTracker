package com.riju.drivertracker.ui.triphistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.triphistory.components.TripHistoryItem
import com.riju.drivertracker.ui.uicomponents.DTConfirmDialog
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar
import com.riju.drivertracker.ui.uicomponents.DTTopBarActionButton
import com.riju.drivertracker.ui.uicomponents.DTTopBarActionButton.ActionIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TripHistoryScreen(
    viewModel: TripHistoryViewModel,
    onTripSelected: (String) -> Unit,
) {
    val isOrderAscending by viewModel.isOrderAscending.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val emptyTripDialog by viewModel.emptyTripDialog.collectAsStateWithLifecycle()
    val tripHistoryList by viewModel.tripHistoryList.collectAsStateWithLifecycle()

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
                        viewModel.toggleSortOrder()
                    },
                    contentDescription = "Sort"
                )
            )
        )
    ) {
        if (tripHistoryList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.trip_history_no_trips_found),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tripHistoryList.forEach { (date, tripList) ->
                    stickyHeader {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(start = 16.dp),
                            style = MaterialTheme.typography.titleLarge,
                            text = date.toString()
                        )
                    }
                    items(
                        items = tripList,
                        key = { it.tripId }
                    ) { trip ->
                        TripHistoryItem(
                            onTripSelected = { tripId ->
                                coroutineScope.launch {
                                    if (viewModel.isTripEmpty(tripId)) {
                                        viewModel.setEmptyTripDialog(tripId)
                                    } else {
                                        onTripSelected.invoke(tripId)
                                    }
                                }
                            },
                            trip = trip,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }

    emptyTripDialog?.let { dialogTripId ->
        EmptyTripDialog(
            tripId = dialogTripId,
            onConfirmDelete = { tripId ->
                viewModel.deleteTrip(tripId)
                viewModel.setEmptyTripDialog(null)
            },
            onDismissDialog = {
                viewModel.setEmptyTripDialog(null)
            }
        )
    }
}

@Composable
fun EmptyTripDialog(
    tripId: String,
    onConfirmDelete: (String) -> Unit,
    onDismissDialog: () -> Unit,
) {
    DTConfirmDialog(
        title = "This trip has no route",
        text = "This trip has no route. Do you want to delete it?",
        confirmButtonText = "Delete",
        dismissButtonText = "Cancel",
        onConfirmButton = {
            onConfirmDelete(tripId)
        },
        onDismiss = onDismissDialog
    )
}
