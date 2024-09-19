package com.riju.drivertracker.datasource.model.routepoint

data class RoutePointResponse(
    override val latitude: Double = 0.0,
    override val longitude: Double = 0.0,
    override val speed: Double = 0.0
) : RoutePointApiModel()
