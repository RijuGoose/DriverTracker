package com.riju.drivertracker.ui

sealed class Screen(val route: String) {
    data object Login: Screen("login")
    data object Register: Screen("register")
    data object Map: Screen("map")
}