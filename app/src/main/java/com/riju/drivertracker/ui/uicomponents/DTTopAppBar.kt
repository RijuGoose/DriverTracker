package com.riju.drivertracker.ui.uicomponents

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class DTTopAppBar(
    val title: String,
    val onBackButtonClicked: (() -> Unit)? = null,
    val actionButtons: List<DTTopBarActionButton>? = null
)

data class DTTopBarActionButton(
    val icon: ActionIcon,
    val onClick: () -> Unit,
    val contentDescription: String
) {
    data class ActionIcon(
        val icon: ImageVector,
        val modifier: Modifier = Modifier,
    )
}
