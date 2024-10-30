package com.riju.remotedatasourceimpl.model.routepoint

data class RoutePointRequest(
    override val latitude: Double,
    override val longitude: Double,
    override val speed: Double
) : RoutePointApiModel()
