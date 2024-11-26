package com.riju.drivertracker.extensions

import java.math.RoundingMode

fun Double.roundToDecimalPlaces(decimalPlaces: Int): Double {
    return toBigDecimal().setScale(decimalPlaces, RoundingMode.HALF_UP).toDouble()
}
