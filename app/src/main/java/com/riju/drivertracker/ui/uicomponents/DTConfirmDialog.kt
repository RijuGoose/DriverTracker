package com.riju.drivertracker.ui.uicomponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DTConfirmDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirmButton: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Text(text)
        },
        confirmButton = {
            TextButton(onClick = onConfirmButton) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissButtonText)
            }
        }
    )
}
