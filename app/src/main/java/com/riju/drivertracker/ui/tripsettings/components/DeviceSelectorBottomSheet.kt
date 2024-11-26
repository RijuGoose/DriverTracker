package com.riju.drivertracker.ui.tripsettings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.riju.repository.model.BTDevice
import com.riju.repository.model.UserPermissionState
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DeviceSelectorBottomSheet(
    pairedDevices: UserPermissionState<List<BTDevice>>,
    onHideBottomSheet: () -> Unit,
    onDeviceSelected: (BTDevice) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onHideBottomSheet,
        sheetState = sheetState,
        dragHandle = {
            DeviceSelectorDragHandle()
        }
    ) {
        when (pairedDevices) {
            is UserPermissionState.Granted -> {
                if (pairedDevices.value.isEmpty()) {
                    Text(
                        text = "No paired devices found",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(pairedDevices.value) { device ->
                            DeviceListItem(
                                onDeviceSelected = { selectedDevice ->
                                    onDeviceSelected.invoke(selectedDevice)
                                    coroutineScope
                                        .launch { sheetState.hide() }
                                        .invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                onHideBottomSheet.invoke()
                                            }
                                        }
                                },
                                device = device
                            )
                        }
                    }
                }
            }

            is UserPermissionState.Denied -> {
                Text("Permission denied")
            }
        }
    }
}

@Composable
private fun DeviceListItem(
    onDeviceSelected: (BTDevice) -> Unit,
    device: BTDevice,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onDeviceSelected.invoke(device)
            }
            .padding(10.dp)
    ) {
        Text(text = device.name)
    }
}

@Composable
private fun DeviceSelectorDragHandle() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Select from paired devices",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(4.dp))
    }
}
