package com.riju.drivertracker.ui.tripsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
        uiModel.automaticTrip,
        bluetoothPermissionState.status,
        backgroundLocationPermissionState.status
    ) {
        mutableStateOf(
            uiModel.automaticTrip &&
                bluetoothPermissionState.status.isGranted &&
                backgroundLocationPermissionState.status.isGranted
        )
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
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExpandableSwitch(
                textFieldValue = uiModel.btDeviceName,
                textFieldLabel = stringResource(R.string.trip_settings_automatic_trip_bt_device_name_label),
                switchText = stringResource(R.string.trip_settings_automatic_trip_switch),
                switchDescription = stringResource(R.string.trip_settings_automatic_trip_description),
                isChecked = bluetoothChecked,
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
                },
                onTextFieldChange = viewModel::setBluetoothDeviceName,
                textFieldCondition = viewModel.tripSecondsConditionValid()
            )

//            SwitchCard(
//                text = "Add trip events to calendar",
//                checked = uiModel.calendarEvent,
//                onCheckedChange = viewModel::setTripCalendarEvent
//            ) // TODO add calendar event

            ExpandableSwitch(
                textFieldValue = uiModel.mergeTripSeconds?.toString(),
                textFieldLabel = stringResource(R.string.trip_settings_merge_trip_duration_seconds_label),
                textFieldCondition = uiModel.mergeTripSeconds in 1..300,
                switchText = stringResource(R.string.trip_settings_merge_trips_switch),
                switchDescription = stringResource(R.string.trip_settings_merge_trip_description),
                supportingText = stringResource(R.string.trip_settings_merge_trip_field_supporting_text),
                isChecked = uiModel.shouldMergeTrips,
                onCheckedChange = viewModel::setShouldMergeTrips,
                onTextFieldChange = { value ->
                    if (value.isDigitsOnly()) {
                        viewModel.setMergeTripSeconds(value)
                    }
                }
            )
        }

        if (backgroundLocationPermissionDialog) {
            PermissionAlertDialog(
                permissionName = stringResource(R.string.trip_settings_bg_location_permission),
                onConfirmButton = {
                    backgroundLocationPermissionState.launchPermissionRequest()
                    viewModel.hideBackgroundLocationPermissionDialog()
                },
                onDismissDialog = viewModel::hideBackgroundLocationPermissionDialog
            )
        }

        if (bluetoothPermissionDialog) {
            PermissionAlertDialog(
                permissionName = stringResource(R.string.trip_settings_bt_permission),
                onConfirmButton = {
                    context.openSettings()
                    viewModel.hideBluetoothPermissionDialog()
                },
                onDismissDialog = viewModel::hideBluetoothPermissionDialog
            )
        }
    }
}
