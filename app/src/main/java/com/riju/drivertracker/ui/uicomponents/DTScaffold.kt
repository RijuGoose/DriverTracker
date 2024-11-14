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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun <T : Any> DTScaffold(
    viewModel: BaseViewModel<T>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    horizontalPadding: Dp = 8.dp,
    topBar: DTTopAppBar? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable BoxScope.(T) -> Unit
) {
    val screenStatus by viewModel.screenStatus.collectAsStateWithLifecycle()
    val showLoadingDialog by viewModel.showLoadingDialog.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.showPullToRefresh.collectAsStateWithLifecycle()

    DTScaffoldSnackBarHost(
        screenStatus = screenStatus,
        modifier = modifier,
        showLoadingDialog = showLoadingDialog,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        topBar = topBar,
        snackBarState = viewModel.snackBarState,
        errorState = viewModel.errorState,
        horizontalPadding = horizontalPadding,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content
    )
}

@Composable
private fun <T : Any> DTScaffoldSnackBarHost(
    screenStatus: ScreenStatus<T>,
    modifier: Modifier = Modifier,
    errorState: SharedFlow<String>,
    showLoadingDialog: Boolean = false,
    snackBarState: SharedFlow<String>,
    onRefresh: () -> Unit = {},
    isRefreshing: Boolean = false,
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
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        errorState.collectLatest { error ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(error)
            }
        }
    }

    LaunchedEffect(Unit) {
        snackBarState.collectLatest { snackBar ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(snackBar)
            }
        }
    }

    DTScaffoldTopAppBar(
        screenStatus = screenStatus,
        modifier = modifier,
        showLoadingDialog = showLoadingDialog,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        topBar = topBar,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        horizontalPadding = horizontalPadding,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T : Any> DTScaffoldTopAppBar(
    screenStatus: ScreenStatus<T>,
    modifier: Modifier = Modifier,
    showLoadingDialog: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    isRefreshing: Boolean = false,
    horizontalPadding: Dp = 8.dp,
    topBar: DTTopAppBar? = null,
    snackbarHost: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable BoxScope.(T) -> Unit
) {
    DTScaffoldPullToRefresh(
        screenStatus = screenStatus,
        modifier = modifier,
        showLoadingDialog = showLoadingDialog,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        topBar = {
            if (topBar != null) {
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
                            IconButton(onClick = actionButton.onClick::invoke) {
                                Icon(
                                    imageVector = actionButton.icon.icon,
                                    modifier = actionButton.icon.modifier,
                                    contentDescription = actionButton.contentDescription
                                )
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = snackbarHost,
        horizontalPadding = horizontalPadding,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> DTScaffoldPullToRefresh(
    screenStatus: ScreenStatus<T>,
    modifier: Modifier = Modifier,
    showLoadingDialog: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    isRefreshing: Boolean = false,
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit,
    horizontalPadding: Dp = 8.dp,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (BoxScope.(T) -> Unit)
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets
    ) {
        PullToRefreshBox(
            isRefreshing = onRefresh != null && isRefreshing,
            onRefresh = {
                onRefresh?.invoke()
            },
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
        ) {
            DTScaffoldContent(screenStatus, content, showLoadingDialog)
        }
    }
}

@Composable
private fun <T : Any> BoxScope.DTScaffoldContent(
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
