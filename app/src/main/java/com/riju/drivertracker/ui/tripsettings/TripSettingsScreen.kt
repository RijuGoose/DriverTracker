package com.riju.drivertracker.ui.tripsettings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.riju.drivertracker.ui.tripsettings.components.SwitchCard
import com.riju.drivertracker.ui.uicomponents.DTOutlinedTextField
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TripSettingsScreen(viewModel: TripSettingsViewModel, onBackButtonClicked: (() -> Unit)?) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val btDeviceName by viewModel.btDeviceName.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val bluetoothPermissionState =
        rememberPermissionState(
            permission = android.Manifest.permission.BLUETOOTH_CONNECT,
            onPermissionResult = { granted ->
                viewModel.setAutomaticTrip(granted)
            }
        )

    DTScaffold(
        viewModel = viewModel,
        topBar = DTTopAppBar(
            title = "Trip settings",
            onBackButtonClicked = onBackButtonClicked
        )
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedCard {
                SwitchCard(
                    text = "Automatic trip",
                    checked = settings.automaticTrip && bluetoothPermissionState.status.isGranted,
                    onCheckedChange = { checked ->
                        if (bluetoothPermissionState.status.isGranted) {
                            viewModel.setAutomaticTrip(checked)
                        } else if (bluetoothPermissionState.status.shouldShowRationale &&
                            bluetoothPermissionState.status.isGranted.not()
                        ) {
                            // TODO refactor
                            val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", context.packageName, null)
                            settingsIntent.data = uri
                            context.startActivity(settingsIntent)
                        } else {
                            bluetoothPermissionState.launchPermissionRequest()
                        }
                    }
                )

                AnimatedVisibility(visible = settings.automaticTrip && bluetoothPermissionState.status.isGranted) {
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
