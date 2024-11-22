package com.riju.drivertracker.ui.uicomponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DTTopBar(topBar: DTTopAppBar) {
    TopAppBar(
        title = {
            Text(topBar.title)
        },
        navigationIcon = {
            if (topBar.onBackButtonClicked != null) {
                IconButton(onClick = topBar.onBackButtonClicked::invoke) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            topBar.actionButtons?.forEach { actionButton ->
                actionButton.icon?.let { icon ->
                    IconButton(onClick = actionButton.onClick::invoke) {
                        Icon(
                            imageVector = icon.icon,
                            modifier = icon.modifier,
                            contentDescription = actionButton.contentDescription
                        )
                    }
                }
                actionButton.text?.let {
                    IconButton(onClick = actionButton.onClick::invoke) {
                        Text(text = actionButton.text)
                    }
                }
            }
        }
    )
}
