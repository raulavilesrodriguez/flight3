package com.example.flight3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flight3.data.FlightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FlightViewModel(
    savedStateHandle: SavedStateHandle,
    private val flightRepository: FlightRepository
) : ViewModel(){

    private val airportId: Int = checkNotNull(savedStateHandle[FlightDestination.flightIdArg])


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val routesUiState: StateFlow<FlightUiState> =
        flightRepository.getFlightsStream(airportId)
            .filterNotNull()
            .map {
                FlightUiState(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FlightUiState()
            )

}

data class RoutesUiState(
    val nameDeparture: String = "",
    val departureCode: String = "",
    val nameDestination: String = "",
    val destinationCode: String = "",
    val inFavorite: String = "",
)

/**
 * Ui State for Routes
 */
data class FlightUiState(
    val airportsRoutes: List<RoutesUiState> = listOf(),
)