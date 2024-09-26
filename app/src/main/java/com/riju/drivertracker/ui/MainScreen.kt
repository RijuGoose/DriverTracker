package com.riju.drivertracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import com.riju.drivertracker.MainViewModel
import com.riju.drivertracker.R
import com.riju.drivertracker.ui.currenttrip.CurrentTripScreen
import com.riju.drivertracker.ui.currenttrip.CurrentTripViewModel
import com.riju.drivertracker.ui.mainmenu.MainMenuScreen
import com.riju.drivertracker.ui.mainmenu.MainMenuViewModel
import com.riju.drivertracker.ui.navigation.Screen
import com.riju.drivertracker.ui.navigation.TopLevelRoute
import com.riju.drivertracker.ui.tripdetails.TripDetailsScreen
import com.riju.drivertracker.ui.tripdetails.TripDetailsViewModel
import com.riju.drivertracker.ui.triphistory.TripHistoryScreen
import com.riju.drivertracker.ui.triphistory.TripHistoryViewModel

@Composable
fun MainScreen(
    mainNavHostController: NavHostController,
    onLogout: () -> Unit,
    mainViewModel: MainViewModel
) {
    Scaffold(
        bottomBar = {
            DTBottomNavigationBar(navController = mainNavHostController)
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavHostController,
            startDestination = Screen.Main,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            navigation<Screen.Main>(startDestination = mainViewModel.mainStartScreen) {
                composable<Screen.CurrentTrip> {
                    val viewModel = hiltViewModel<CurrentTripViewModel>()
                    CurrentTripScreen(
                        viewModel = viewModel,
                    )
                }
            }
            navigation<Screen.TripHistory>(startDestination = Screen.TripList) {
                composable<Screen.TripList> {
                    val viewModel = hiltViewModel<TripHistoryViewModel>()
                    TripHistoryScreen(
                        viewModel = viewModel,
                        onTripSelected = { tripId ->
                            mainNavHostController.navigate(Screen.TripDetails(tripId = tripId))
                        }
                    )
                }

                composable<Screen.TripDetails> {
                    val viewModel = hiltViewModel<TripDetailsViewModel>()
                    TripDetailsScreen(
                        viewModel = viewModel,
                        onBackPressed = { mainNavHostController.navigateUp() }
                    )
                }
            }

            navigation<Screen.Menu>(startDestination = Screen.MainMenu) {
                composable<Screen.MainMenu> {
                    val viewModel = hiltViewModel<MainMenuViewModel>()
                    MainMenuScreen(
                        viewModel = viewModel,
                        onLogout = onLogout,
                    )
                }
            }
        }
    }
}

@Composable
private fun DTBottomNavigationBar(navController: NavHostController) {
    val topLevelRoutes = listOf(
        TopLevelRoute(
            stringResource(R.string.navigation_current_trip),
            Screen.CurrentTrip(),
            Icons.Outlined.LocationOn
        ),
        TopLevelRoute(
            stringResource(R.string.navigation_trip_history),
            Screen.TripHistory,
            Icons.Outlined.Refresh
        ),
        TopLevelRoute(
            stringResource(R.string.navigation_main_menu),
            Screen.MainMenu,
            Icons.Outlined.Menu
        )
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                label = { Text(topLevelRoute.name) },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(topLevelRoute.route::class)
                } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
