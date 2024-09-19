package com.riju.drivertracker.datasource.model.routepoint

data class RoutePointRequest(
    override val latitude: Double,
    override val longitude: Double,
    override val speed: Double
) : RoutePointApiModel()
