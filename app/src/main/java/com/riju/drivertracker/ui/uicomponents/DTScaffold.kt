package com.riju.drivertracker.ui.uicomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> DTScaffold(
    viewModel: BaseViewModel<T>,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 8.dp,
    topBar: DTTopAppBar? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable BoxScope.(T) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val screenStatus by viewModel.screenStatus.collectAsStateWithLifecycle()
    val showLoadingDialog by viewModel.showLoadingDialog.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.errorState.collectLatest { error ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(error)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.snackBarState.collectLatest { snackBar ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(snackBar)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (topBar != null) {
                TopAppBar(
                    title = {
                        Text(topBar.title)
                    },
                    navigationIcon = {
                        if (topBar.onBackButtonClicked != null) {
                            IconButton(
                                onClick = {
                                    topBar.onBackButtonClicked.invoke()
                                }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
        ) {
            when (val status = screenStatus) {
                is ScreenStatus.LoadingFullScreen -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ScreenStatus.Success -> {
                    content(this, status.value)
                }

                is ScreenStatus.ErrorFullScreen -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = status.error)
                    }
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
    }
}
