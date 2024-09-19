package com.riju.drivertracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.riju.drivertracker.service.LocationService

class TripManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moveTaskToBack(true)
        val status = intent.getStringExtra("tripStatus")
        Log.d("libalog-status", status.toString())
        Intent(this, LocationService::class.java).apply {
            action = status
            startService(this)
        }
        finish()
    }
}
