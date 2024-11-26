package com.riju.drivertracker.ui.uicomponents

import androidx.compose.runtime.Composable

@Composable
fun PermissionAlertDialog(
    showDialog: Boolean,
    permissionName: String,
    onConfirmButton: () -> Unit,
    onDismissDialog: () -> Unit
) {
    if (showDialog) {
        DTAlertDialog(
            title = "$permissionName permission required",
            text = "$permissionName permission is required for this feature. " +
                "Please enable it in the app settings.",
            onDismissDialog = onDismissDialog,
            confirmButtonText = "Open settings",
            onConfirmButton = onConfirmButton
        )
    }
}
