package com.riju.drivertracker.ui.tripsettings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.ui.tripsettings.components.SwitchCard
import com.riju.drivertracker.ui.uicomponents.DTOutlinedTextField
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar

@Composable
fun TripSettingsScreen(viewModel: TripSettingsViewModel, onBackButtonClicked: (() -> Unit)?) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val btDeviceName by viewModel.btDeviceName.collectAsStateWithLifecycle()

    DTScaffold(
        viewModel = viewModel,
        topBar = DTTopAppBar(
            title = "Trip settings",
            onBackButtonClicked = onBackButtonClicked
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedCard {
                SwitchCard(
                    text = "Automatic trip",
                    checked = settings.automaticTrip,
                    onCheckedChange = viewModel::setAutomaticTrip
                )

                AnimatedVisibility(visible = settings.automaticTrip) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors().copy(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Box(modifier = Modifier.padding(8.dp)) {
                                Text(text = "Start a trip when the phone is connected to a bluetooth device")
                            }
                        }
                        DTOutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = btDeviceName,
                            onValueChange = viewModel::setBluetoothDeviceName,
                            label = "Bluetooth device name"
                        )
                    }
                }
            }
            SwitchCard(
                text = "Add trip events to calendar",
                checked = settings.calendarEvent,
                onCheckedChange = viewModel::setTripCalendarEvent
            ) // TODO add calendar event
        }
    }
}
