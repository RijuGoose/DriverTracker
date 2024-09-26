package com.riju.drivertracker.ui.uicomponents

data class DTTopAppBar(
    val title: String,
    val onBackButtonClicked: (() -> Unit)? = null
)
