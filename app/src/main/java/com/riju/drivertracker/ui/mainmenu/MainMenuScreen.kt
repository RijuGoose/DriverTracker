package com.riju.drivertracker.ui.mainmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.mainmenu.components.MainMenuButton
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel,
    onLogout: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.onLogoutSuccess.collectLatest {
            onLogout()
        }
    }

    DTScaffold(viewModel = viewModel) {
        Column {
            MainMenuButton(title = stringResource(R.string.main_menu_button_logout), onClick = viewModel::logout)
        }
    }
}
