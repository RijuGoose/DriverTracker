package com.riju.drivertracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.riju.drivertracker.ui.CurrentTripAction
import com.riju.drivertracker.ui.Screen
import com.riju.drivertracker.ui.currenttrip.CurrentTripScreen
import com.riju.drivertracker.ui.currenttrip.CurrentTripViewModel
import com.riju.drivertracker.ui.login.LoginScreen
import com.riju.drivertracker.ui.login.LoginViewModel
import com.riju.drivertracker.ui.map.MapScreen
import com.riju.drivertracker.ui.map.MapViewModel
import com.riju.drivertracker.ui.register.RegisterScreen
import com.riju.drivertracker.ui.register.RegisterViewModel
import com.riju.drivertracker.ui.tripdetails.TripDetailsScreen
import com.riju.drivertracker.ui.tripdetails.TripDetailsViewModel
import com.riju.drivertracker.ui.triphistory.TripHistoryScreen
import com.riju.drivertracker.ui.triphistory.TripHistoryViewModel

@Composable
fun DTNavHost(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = mainViewModel.startScreen,
        modifier = modifier
    ) {
        composable<Screen.Login> {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                viewModel = viewModel,
                onRegisterClicked = {
                    navHostController.navigate(Screen.Register)
                },
                onLoginSuccess = {
                    navHostController.navigate(Screen.Map) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable<Screen.Register> {
            val viewModel = hiltViewModel<RegisterViewModel>()
            RegisterScreen(
                viewModel = viewModel,
                onRegistrationSuccess = {
                    navHostController.navigate(Screen.Map) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable<Screen.Map> {
            val viewModel = hiltViewModel<MapViewModel>()
            MapScreen(
                viewModel = viewModel,
                onLogout = {
                    navHostController.navigate(Screen.Login) {
                        popUpTo(0)
                    }
                },
                navigateToTripHistory = {
                    navHostController.navigate(Screen.TripHistory)
                },
                navigateToCurrentTrip = {
                    navHostController.navigate(Screen.CurrentTrip(action = CurrentTripAction.None))
                }
            )
        }

        composable<Screen.TripHistory> {
            val viewModel = hiltViewModel<TripHistoryViewModel>()
            TripHistoryScreen(
                viewModel = viewModel,
                onTripSelected = { tripId ->
                    navHostController.navigate(Screen.TripDetails(tripId = tripId))
                }
            )
        }

        composable<Screen.TripDetails> {
            val viewModel = hiltViewModel<TripDetailsViewModel>()
            TripDetailsScreen(
                viewModel = viewModel
            )
        }

        composable<Screen.CurrentTrip> {
            val viewModel = hiltViewModel<CurrentTripViewModel>()
            CurrentTripScreen(
                viewModel = viewModel
            )
        }
    }
}
