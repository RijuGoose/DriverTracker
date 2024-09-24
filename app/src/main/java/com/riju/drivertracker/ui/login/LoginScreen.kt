package com.riju.drivertracker.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riju.drivertracker.ui.theme.Typography
import com.riju.drivertracker.ui.uicomponents.DTOutlinedTextField
import com.riju.drivertracker.ui.uicomponents.DTScaffold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    val userName by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onSuccessLogin.collectLatest {
            onLoginSuccess()
        }
    }

    DTScaffold(
        viewModel = viewModel,
        horizontalPadding = 16.dp
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Driver Tracker",
                    style = Typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DTOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userName,
                    onValueChange = viewModel::setEmail,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    label = "Email"
                )
                DTOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = viewModel::setPassword,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.login(userName, password)
                        }
                    ),
                    label = "Password"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.login(userName, password)
                    }
                ) {
                    Text(text = "Login")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRegisterClicked
                ) {
                    Text(text = "Register")
                }
            }
        }
    }
}
