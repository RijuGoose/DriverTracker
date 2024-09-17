package com.riju.drivertracker.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.ui.DTScaffold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onSuccessLogin.collectLatest {
            onLoginSuccess()
        }
    }

    DTScaffold(viewModel = viewModel) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = userName,
                onValueChange = viewModel::setUserName,
                singleLine = true
            )
            OutlinedTextField(
                value = password,
                onValueChange = viewModel::setPassword,
                singleLine = true
            )
            Button(
                onClick = {
                    viewModel.login(userName, password)
                }
            ) {
                Text(text = "Login")
            }
            Button(
                onClick = onRegisterClicked
            ) {
                Text(text = "Register")
            }
        }
    }
}
