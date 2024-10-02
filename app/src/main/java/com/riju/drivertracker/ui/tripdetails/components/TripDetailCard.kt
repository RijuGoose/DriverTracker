package com.riju.drivertracker.ui.tripdetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.ui.theme.DriverTrackerTheme

@Composable
fun TripDetailCard(
    cardTitle: String,
    cardValue: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = cardTitle, fontWeight = FontWeight.Bold)
            Text(text = cardValue, textAlign = TextAlign.Center)
        }
    }
}

@Preview
@Composable
fun TripDetailCardPreview() {
    DriverTrackerTheme {
        TripDetailCard(
            modifier = Modifier.fillMaxWidth(),
            cardTitle = "Avg Speed",
            cardValue = "50 km/h"
        )
    }
}
