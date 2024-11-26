package com.riju.drivertracker.ui.uicomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    onRefresh: (() -> Unit)? = null,
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
    onRefresh: (() -> Unit)? = null,
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
                DTTopBar(topBar)
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
        if (onRefresh != null) {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize()
            ) {
                DTScaffoldContent(screenStatus, content, showLoadingDialog)
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize()
            ) {
                DTScaffoldContent(screenStatus, content, showLoadingDialog)
            }
        }
    }
}
