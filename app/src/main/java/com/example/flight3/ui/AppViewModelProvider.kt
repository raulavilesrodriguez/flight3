package com.example.flight3.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flight3.FlightApplication

/**
 * Provides Factory to create instance of ViewModel for the entire Flight app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for AirportViewModel
        initializer {
            AirportViewModel(
                flightApplication().container.flightRepository,
                flightApplication().userPreferencesRepository
            )
        }

        // Initializer for FlighViewModel
        initializer {
            FlightViewModel(
                this.createSavedStateHandle(),
                flightApplication().container.flightRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [FlightApplication].
 */
fun CreationExtras.flightApplication(): FlightApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightApplication)