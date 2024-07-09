package com.example.flight3.data

import android.content.Context

interface AppContainer {
    val flightRepository : FlightRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val flightRepository: FlightRepository by lazy {
        OfflineFlightsRepository(FlightDatabase.getDatabase(context).flightDao())
    }
}