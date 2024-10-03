package com.riju.drivertracker.ui.tripsettings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.ui.theme.DriverTrackerTheme

@Composable
fun SwitchCard(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = text
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Preview
@Composable
private fun SwitchCardPreview() {
    DriverTrackerTheme {
        SwitchCard(
            text = "Automatic trip",
            checked = true,
            onCheckedChange = {}
        )
    }
}

@Preview
@Composable
private fun SwitchCardPreview2() {
    DriverTrackerTheme {
        SwitchCard(
            text = "Automatic tripAutomatic tripAutomatic tripAutomatic tripAutomatic tripAutomatic trip",
            checked = true,
            onCheckedChange = {}
        )
    }
}
