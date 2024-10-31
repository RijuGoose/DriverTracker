package com.riju.drivertracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.riju.drivertracker.MainViewModel
import com.riju.drivertracker.ui.MainScreen
import com.riju.drivertracker.ui.login.LoginScreen
import com.riju.drivertracker.ui.login.LoginViewModel
import com.riju.drivertracker.ui.register.RegisterScreen
import com.riju.drivertracker.ui.register.RegisterViewModel

@Composable
fun DTNavHost(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = mainViewModel.startScreen,
        modifier = modifier
    ) {
        navigation<Screen.Auth>(startDestination = Screen.Login) {
            composable<Screen.Login> {
                val viewModel = hiltViewModel<LoginViewModel>()
                LoginScreen(
                    viewModel = viewModel,
                    onRegisterClicked = {
                        navHostController.navigate(Screen.Register)
                    },
                    onLoginSuccess = {
                        navHostController.navigate(Screen.Main) {
                            popUpTo(0)
                        }
                    },
                    navigateWithoutLogin = {
                        navHostController.navigate(Screen.Main)
                    }
                )
            }
            composable<Screen.Register> {
                val viewModel = hiltViewModel<RegisterViewModel>()
                RegisterScreen(
                    viewModel = viewModel,
                    onRegistrationSuccess = {
                        navHostController.navigate(Screen.Main) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }

        composable<Screen.Main> {
            val mainNavHostController = rememberNavController()
            MainScreen(
                mainViewModel = mainViewModel,
                mainNavHostController = mainNavHostController,
                onLogout = {
                    navHostController.navigate(Screen.Login) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}
