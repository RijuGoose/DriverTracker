package com.riju.drivertracker.ui.uicomponents

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.ui.BaseViewModel
import com.riju.drivertracker.ui.ScreenStatus
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun <T : Any> DTBottomSheetScaffold(
    viewModel: BaseViewModel<T>,
    sheetContent: @Composable ColumnScope.(T) -> Unit,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    horizontalPadding: Dp = 8.dp,
    topBar: DTTopAppBar? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable BoxScope.(T) -> Unit
) {
    val screenStatus by viewModel.screenStatus.collectAsStateWithLifecycle()
    val showLoadingDialog by viewModel.showLoadingDialog.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.showPullToRefresh.collectAsStateWithLifecycle()

    DTBottomSheetScaffoldSnackBarHost(
        screenStatus = screenStatus,
        sheetContent = sheetContent,
        modifier = modifier,
        showLoadingDialog = showLoadingDialog,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        topBar = topBar,
        snackBarState = viewModel.snackBarState,
        errorState = viewModel.errorState,
        horizontalPadding = horizontalPadding,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content
    )
}

@Composable
private fun <T : Any> DTBottomSheetScaffoldSnackBarHost(
    screenStatus: ScreenStatus<T>,
    sheetContent: @Composable (ColumnScope.(T) -> Unit),
    modifier: Modifier = Modifier,
    errorState: SharedFlow<String>,
    showLoadingDialog: Boolean = false,
    snackBarState: SharedFlow<String>,
    onRefresh: () -> Unit = {},
    isRefreshing: Boolean = false,
    horizontalPadding: Dp = 8.dp,
    topBar: DTTopAppBar? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
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

    DTBottomSheetScaffoldTopAppBar(
        screenStatus = screenStatus,
        sheetContent = sheetContent,
        modifier = modifier,
        showLoadingDialog = showLoadingDialog,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        horizontalPadding = horizontalPadding,
        topBar = topBar,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T : Any> DTBottomSheetScaffoldTopAppBar(
    screenStatus: ScreenStatus<T>,
    sheetContent: @Composable (ColumnScope.(T) -> Unit),
    modifier: Modifier = Modifier,
    showLoadingDialog: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    isRefreshing: Boolean = false,
    horizontalPadding: Dp = 8.dp,
    topBar: DTTopAppBar? = null,
    snackbarHost: @Composable (SnackbarHostState) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (BoxScope.(T) -> Unit),
) {
    DTBottomSheetScaffoldPullToRefresh(
        screenStatus = screenStatus,
        sheetContent = sheetContent,
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
        containerColor = containerColor,
        contentColor = contentColor,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> DTBottomSheetScaffoldPullToRefresh(
    screenStatus: ScreenStatus<T>,
    sheetContent: @Composable (ColumnScope.(T) -> Unit),
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    sheetPeekHeight: Dp = BottomSheetDefaults.SheetPeekHeight,
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    sheetShape: Shape = BottomSheetDefaults.ExpandedShape,
    sheetContainerColor: Color = BottomSheetDefaults.ContainerColor,
    sheetContentColor: Color = contentColorFor(sheetContainerColor),
    sheetTonalElevation: Dp = 0.dp,
    sheetShadowElevation: Dp = BottomSheetDefaults.Elevation,
    sheetDragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    sheetSwipeEnabled: Boolean = true,
    showLoadingDialog: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    isRefreshing: Boolean = false,
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit,
    horizontalPadding: Dp = 8.dp,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (BoxScope.(T) -> Unit)
) {
    BottomSheetScaffold(
        modifier = modifier,
        sheetContent = {
            when (screenStatus) {
                is ScreenStatus.Success -> {
                    sheetContent(this, screenStatus.value)
                }
                else -> {
                    /* no-op */
                }
            }
        },
        topBar = topBar,
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetMaxWidth = sheetMaxWidth,
        sheetShape = sheetShape,
        sheetContentColor = sheetContentColor,
        sheetTonalElevation = sheetTonalElevation,
        sheetShadowElevation = sheetShadowElevation,
        sheetDragHandle = sheetDragHandle,
        sheetSwipeEnabled = sheetSwipeEnabled,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        contentColor = contentColor,
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
