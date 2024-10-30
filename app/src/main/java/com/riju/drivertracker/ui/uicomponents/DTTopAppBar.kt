package com.riju.drivertracker.ui.uicomponents

import androidx.compose.ui.graphics.vector.ImageVector

data class DTTopAppBar(
    val title: String,
    val onBackButtonClicked: (() -> Unit)? = null,
    val actionButtons: List<DTTopBarActionButton>? = null
)

data class DTTopBarActionButton(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val contentDescription: String
)
