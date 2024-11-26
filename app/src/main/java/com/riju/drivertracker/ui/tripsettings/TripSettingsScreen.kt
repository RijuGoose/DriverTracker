package com.riju.drivertracker.ui.tripsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.BluetoothSearching
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.riju.drivertracker.R
import com.riju.drivertracker.extensions.openSettings
import com.riju.drivertracker.extensions.shouldShowDialog
import com.riju.drivertracker.ui.tripsettings.components.DeviceSelectorBottomSheet
import com.riju.drivertracker.ui.tripsettings.components.ExpandableSwitch
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar
import com.riju.drivertracker.ui.uicomponents.DTTopBarActionButton
import com.riju.drivertracker.ui.uicomponents.PermissionAlertDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TripSettingsScreen(
    viewModel: TripSettingsViewModel,
    onBackButtonClicked: (() -> Unit)?
) {
    val uiModel by viewModel.settingsUiModel.collectAsStateWithLifecycle()

    val backgroundLocationPermissionDialog by viewModel.backgroundLocationPermissionDialog.collectAsStateWithLifecycle()
    val bluetoothPermissionDialog by viewModel.bluetoothPermissionDialog.collectAsStateWithLifecycle()
    val pairedDevicesSheet by viewModel.pairedDevicesSheet.collectAsStateWithLifecycle()
    val pairedDevices by viewModel.btPairedDevices.collectAsStateWithLifecycle()

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
                    viewModel.toggleBackgroundLocationPermissionDialog(true)
                }
            }
        )

    val bluetoothChecked by remember {
        derivedStateOf {
            uiModel.automaticTrip &&
                bluetoothPermissionState.status.isGranted &&
                backgroundLocationPermissionState.status.isGranted
        }
    }

    DTScaffold(
        viewModel = viewModel,
        topBar = DTTopAppBar(
            title = stringResource(R.string.trip_settings_top_bar_title),
            onBackButtonClicked = onBackButtonClicked,
            actionButtons = listOf(
                DTTopBarActionButton(
                    text = stringResource(R.string.common_save),
                    onClick = viewModel::saveChanges,
                    contentDescription = stringResource(R.string.common_save)
                )
            )
        )
    ) {
        TripSettingsScreenBody(
            uiModel = uiModel,
            isBluetoothChecked = bluetoothChecked,
            onAutomaticTripCheckedChange = { checked ->
                if (bluetoothPermissionState.status.isGranted) {
                    if (backgroundLocationPermissionState.status.isGranted) {
                        viewModel.setAutomaticTrip(checked)
                    } else {
                        viewModel.toggleBackgroundLocationPermissionDialog(true)
                    }
                } else if (bluetoothPermissionState.shouldShowDialog()) {
                    viewModel.toggleBluetoothPermissionDialog(true)
                } else {
                    bluetoothPermissionState.launchPermissionRequest()
                }
            },
            onBluetoothDeviceNameChange = viewModel::setBluetoothDeviceName,
            onBluetoothTrailIconClick = {
                viewModel.getPairedBTDevices()
                viewModel.toggleBluetoothDeviceSheet(true)
            },
            tripSecondsConditionValid = viewModel.tripSecondsConditionValid(),
            onMergeTripsCheckedChange = viewModel::setShouldMergeTrips,
            onMergeTripSecondsChange = { value ->
                if (value.isDigitsOnly()) {
                    viewModel.setMergeTripSeconds(value)
                }
            }
        )

        PermissionAlertDialog(
            showDialog = backgroundLocationPermissionDialog,
            permissionName = stringResource(R.string.trip_settings_bg_location_permission),
            onConfirmButton = {
                backgroundLocationPermissionState.launchPermissionRequest()
                viewModel.toggleBackgroundLocationPermissionDialog(false)
            },
            onDismissDialog = {
                viewModel.toggleBackgroundLocationPermissionDialog(false)
            }
        )

        PermissionAlertDialog(
            showDialog = bluetoothPermissionDialog,
            permissionName = stringResource(R.string.trip_settings_bt_permission),
            onConfirmButton = {
                context.openSettings()
                viewModel.toggleBluetoothPermissionDialog(false)
            },
            onDismissDialog = {
                viewModel.toggleBluetoothPermissionDialog(false)
            }
        )

        if (pairedDevicesSheet) {
            DeviceSelectorBottomSheet(
                pairedDevices = pairedDevices,
                onHideBottomSheet = {
                    viewModel.toggleBluetoothDeviceSheet(false)
                },
                onDeviceSelected = { device ->
                    viewModel.setBluetoothDeviceName(device.name)
                }
            )
        }
    }
}

@Composable
private fun TripSettingsScreenBody(
    uiModel: SettingsUiModel,
    isBluetoothChecked: Boolean,
    onAutomaticTripCheckedChange: (Boolean) -> Unit,
    onBluetoothDeviceNameChange: (String) -> Unit,
    onBluetoothTrailIconClick: () -> Unit,
    tripSecondsConditionValid: Boolean,
    onMergeTripsCheckedChange: (Boolean) -> Unit,
    onMergeTripSecondsChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExpandableSwitch(
            textFieldValue = uiModel.btDeviceName,
            textFieldLabel = stringResource(R.string.trip_settings_automatic_trip_bt_device_name_label),
            switchText = stringResource(R.string.trip_settings_automatic_trip_switch),
            switchDescription = stringResource(R.string.trip_settings_automatic_trip_description),
            isChecked = isBluetoothChecked,
            onCheckedChange = onAutomaticTripCheckedChange,
            onTextFieldChange = onBluetoothDeviceNameChange,
            textFieldTrailingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.BluetoothSearching,
                    contentDescription = "Paired devices"
                )
            },
            onTrailingIconClick = onBluetoothTrailIconClick
        )

        ExpandableSwitch(
            textFieldValue = uiModel.mergeTripSeconds?.toString(),
            textFieldLabel = stringResource(R.string.trip_settings_merge_trip_duration_seconds_label),
            textFieldCondition = tripSecondsConditionValid,
            switchText = stringResource(R.string.trip_settings_merge_trips_switch),
            switchDescription = stringResource(R.string.trip_settings_merge_trip_description),
            supportingText = stringResource(R.string.trip_settings_merge_trip_field_supporting_text),
            isChecked = uiModel.shouldMergeTrips,
            onCheckedChange = onMergeTripsCheckedChange,
            onTextFieldChange = onMergeTripSecondsChange
        )
    }
}
