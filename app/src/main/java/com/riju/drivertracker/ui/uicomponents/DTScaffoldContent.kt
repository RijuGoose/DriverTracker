package com.riju.drivertracker.ui.uicomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.riju.drivertracker.ui.ScreenStatus

@Composable
fun <T : Any> BoxScope.DTScaffoldContent(
    screenStatus: ScreenStatus<T>,
    content: @Composable (BoxScope.(T) -> Unit),
    showLoadingDialog: Boolean
) {
    when (screenStatus) {
        is ScreenStatus.LoadingFullScreen -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ScreenStatus.Success -> {
            content(this, screenStatus.value)
        }

        is ScreenStatus.ErrorFullScreen -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = screenStatus.error)
            }
        }

        else -> {
            /* no-op */
        }
    }

    if (showLoadingDialog) {
        Dialog(onDismissRequest = {}) {
            Card {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
