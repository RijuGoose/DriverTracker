package com.riju.drivertracker.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Auth : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Register : Screen()

    @Serializable
    data object Main : Screen()

    @Serializable
    data object TripHistory : Screen()

    @Serializable
    data object TripList : Screen()

    @Serializable
    data class TripDetails(val tripId: String) : Screen()

    @Serializable
    data class CurrentTrip(val action: CurrentTripAction = CurrentTripAction.None) : Screen()

    @Serializable
    data object Menu : Screen()

    @Serializable
    data object MainMenu : Screen()

    @Serializable
    data object TripSettings : Screen()
}

enum class CurrentTripAction {
    Start, Stop, None
}
