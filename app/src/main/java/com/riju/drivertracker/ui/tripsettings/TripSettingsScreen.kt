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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.riju.drivertracker.extensions.shouldShowDialog
import com.riju.drivertracker.ui.tripsettings.components.SwitchCard
import com.riju.drivertracker.ui.uicomponents.DTAlertDialog
import com.riju.drivertracker.ui.uicomponents.DTOutlinedTextField
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TripSettingsScreen(viewModel: TripSettingsViewModel, onBackButtonClicked: (() -> Unit)?) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val btDeviceName by viewModel.btDeviceName.collectAsStateWithLifecycle()
    val backgroundLocationPermissionDialog by viewModel.backgroundLocationPermissionDialog.collectAsStateWithLifecycle()
    val bluetoothPermissionDialog by viewModel.bluetoothPermissionDialog.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val backgroundLocationPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    val bluetoothPermissionState =
        rememberPermissionState(
            permission = android.Manifest.permission.BLUETOOTH_CONNECT,
            onPermissionResult = { granted ->
                if (backgroundLocationPermissionState.status.isGranted) {
                    viewModel.setAutomaticTrip(granted)
                } else {
                    viewModel.showBackgroundLocationPermissionDialog()
                }
            }
        )

    val bluetoothChecked by remember(
        settings.automaticTrip,
        bluetoothPermissionState.status,
        backgroundLocationPermissionState.status
    ) {
        mutableStateOf(
            settings.automaticTrip &&
                bluetoothPermissionState.status.isGranted &&
                backgroundLocationPermissionState.status.isGranted
        )
    }

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
                    checked = bluetoothChecked,
                    onCheckedChange = { checked ->
                        if (bluetoothPermissionState.status.isGranted) {
                            if (backgroundLocationPermissionState.status.isGranted) {
                                viewModel.setAutomaticTrip(checked)
                            } else {
                                viewModel.showBackgroundLocationPermissionDialog()
                            }
                        } else if (bluetoothPermissionState.shouldShowDialog()) {
                            viewModel.showBluetoothPermissionDialog()
                        } else {
                            bluetoothPermissionState.launchPermissionRequest()
                        }
                    }
                )

                AnimatedVisibility(visible = bluetoothChecked) {
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

        if (backgroundLocationPermissionDialog) {
            PermissionAlertDialog(
                permissionName = "Background location",
                onConfirmButton = {
                    backgroundLocationPermissionState.launchPermissionRequest()
                    viewModel.hideBackgroundLocationPermissionDialog()
                },
                onDismissDialog = viewModel::hideBackgroundLocationPermissionDialog
            )
        }

        if (bluetoothPermissionDialog) {
            PermissionAlertDialog(
                permissionName = "Bluetooth",
                onConfirmButton = {
                    // TODO refact
                    val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    settingsIntent.data = uri
                    context.startActivity(settingsIntent)
                    viewModel.hideBluetoothPermissionDialog()
                },
                onDismissDialog = viewModel::hideBluetoothPermissionDialog
            )
        }
    }
}

@Composable
private fun PermissionAlertDialog(
    permissionName: String,
    onConfirmButton: () -> Unit,
    onDismissDialog: () -> Unit
) {
    DTAlertDialog(
        title = "$permissionName permission required",
        text = "$permissionName permission is required for this feature. " +
            "Please enable it in the app settings.",
        onDismissDialog = onDismissDialog,
        confirmButtonText = "Open settings",
        onConfirmButton = onConfirmButton
    )
}
