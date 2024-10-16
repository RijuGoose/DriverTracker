package com.riju.drivertracker.ui.uicomponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DTAlertDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    onConfirmButton: () -> Unit,
    onDismissDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        title = { Text(title) },
        text = {
            Text(text)
        },
        confirmButton = {
            TextButton(onClick = onConfirmButton) {
                Text(confirmButtonText)
            }
        }
    )
}
