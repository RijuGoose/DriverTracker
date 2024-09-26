package com.riju.drivertracker.ui.mainmenu.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.ui.theme.DriverTrackerTheme

@Composable
fun MainMenuButton(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = title
        )
    }
}

@Preview
@Composable
fun MainMenuButtonPreview() {
    DriverTrackerTheme {
        MainMenuButton(title = "Logout", onClick = {})
    }
}
