package com.riju.drivertracker

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.riju.drivertracker.ui.Screen
import com.riju.drivertracker.ui.login.LoginScreen
import com.riju.drivertracker.ui.login.LoginViewModel
import com.riju.drivertracker.ui.map.MapScreen
import com.riju.drivertracker.ui.map.MapViewModel
import com.riju.drivertracker.ui.register.RegisterScreen
import com.riju.drivertracker.ui.register.RegisterViewModel

@Composable
fun DTNavHost(
    modifier: Modifier,
    snackBarHostState: SnackbarHostState,
    mainViewModel: MainViewModel
) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = mainViewModel.currentUser.value?.let
        { Screen.Map.route }
            ?: Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(viewModel = viewModel,
                snackBarHostState = snackBarHostState,
                onRegisterClicked = {
                    navHostController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navHostController.navigate(Screen.Map.route) {
                        popUpTo(0)
                    }
                })
        }

        composable(Screen.Register.route) {
            val viewModel = hiltViewModel<RegisterViewModel>()
            RegisterScreen(viewModel = viewModel,
                snackBarHostState = snackBarHostState,
                onRegistrationSuccess = {
                    navHostController.navigate(Screen.Map.route) {
                        popUpTo(0)
                    }
                })
        }

        composable(Screen.Map.route) {
            val viewModel = hiltViewModel<MapViewModel>()
            MapScreen(viewModel = viewModel,
                snackBarHostState = snackBarHostState,
                onLogout = {
                    navHostController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                })
        }
    }

}
