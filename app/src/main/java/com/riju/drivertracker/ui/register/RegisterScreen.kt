package com.riju.drivertracker.ui.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.ui.ScreenStatus
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    modifier: Modifier = Modifier,
    onRegistrationSuccess: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.screenStatus.collectLatest {
            when (it) {
                is ScreenStatus.Success -> onRegistrationSuccess()

                is ScreenStatus.Failure -> {
                    snackBarHostState.showSnackbar(it.error)
                }

                else -> {
                    /* no-op */
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = viewModel::setUserName,
            singleLine = true,
        )
        OutlinedTextField(
            value = password,
            onValueChange = viewModel::setPassword,
            singleLine = true
        )
        Button(
            onClick = {
                viewModel.register(userName, password)
            }
        ) {
            Text(text = "Register")
        }
    }
}