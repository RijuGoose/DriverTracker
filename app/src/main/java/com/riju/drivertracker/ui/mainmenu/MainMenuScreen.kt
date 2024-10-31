package com.riju.drivertracker.ui.mainmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.mainmenu.components.MainMenuButton
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import com.riju.drivertracker.ui.uicomponents.DTTopAppBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel,
    onLogout: () -> Unit,
    onAutomaticTripClicked: () -> Unit
) {
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.onLogoutSuccess.collectLatest {
            onLogout()
        }
    }

    DTScaffold(
        viewModel = viewModel,
        topBar = DTTopAppBar(title = "Main menu")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MainMenuButton(
                title = stringResource(R.string.main_menu_button_trip_settings),
                onClick = onAutomaticTripClicked
            )
            if (isUserLoggedIn) {
                MainMenuButton(title = stringResource(R.string.main_menu_button_logout), onClick = viewModel::logout)
            }
        }
    }
}
