package com.riju.drivertracker.repository.model

sealed class UserPermissionState<out T : Any> {
    data class Granted<out T : Any>(val value: T) : UserPermissionState<T>()
    data object Denied : UserPermissionState<Nothing>()
}
