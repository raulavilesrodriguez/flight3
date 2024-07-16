package com.example.flight3.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flight3.data.Airport
import com.example.flight3.data.Favorite
import com.example.flight3.data.FlightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightViewModel(
    savedStateHandle: SavedStateHandle,
    private val flightRepository: FlightRepository
) : ViewModel(){

    private val airportId: Int = checkNotNull(savedStateHandle[FlightDestination.flightIdArg])

    var flyUiState by mutableStateOf(AUiState())
        private set

    private val _rUiState = MutableStateFlow(FlightUiState())
    val routesUiState: StateFlow<FlightUiState> = _rUiState.asStateFlow()

    var favoriteUiState by mutableStateOf(FUiState())
        private set

    init {
        viewModelScope.launch {
            flyUiState = flightRepository.getAirportStream(airportId)
                .filterNotNull()
                .first()
                .toAUiState()

            flightRepository.getFlightsStream(airportId).collect {routes ->
                Log.d("RoutesViewModel", "Routes collected: $routes")
                _rUiState.value = FlightUiState(routes)
            }
        }
    }

    fun updateUiState(aUiState: String){
        flyUiState = AUiState(aUiState)
    }

    fun updateFUiState(favoriteDetails: FavoriteDetails){
        favoriteUiState = FUiState(favoriteDetails = favoriteDetails)
    }

    suspend fun saveFavorite(){
        flightRepository.insertFavorite(favoriteUiState.favoriteDetails.toFavorite())
    }

    suspend fun deleteFavorite(){
        Log.d("DeleteFavorite", "Parametros: ${favoriteUiState.favoriteDetails.toFavorite()}")
        flightRepository.deleteFavorite(favoriteUiState.favoriteDetails.toFavorite())
    }
}

data class AUiState(
    val iataCode: String = "",
)

fun Airport.toAUiState(): AUiState = AUiState(
    iataCode = iataCode,
)

data class RoutesUiState(
    val nameDeparture: String = "",
    val departureCode: String = "",
    val nameDestination: String = "",
    val destinationCode: String = "",
    val inFavorite: String = "",
    val idFavorite: String = ""
)

/**
 * Ui State for Routes
 */
data class FlightUiState(
    val airportsRoutes: List<RoutesUiState> = listOf(),
)

/**
 * To Add and delete of Favorite table
 */
data class FavoriteDetails(
    val id : Int = 0,
    val departureCode: String = "",
    val destinationCode: String = ""
)

data class FUiState(
    val favoriteDetails: FavoriteDetails = FavoriteDetails()
)

fun FavoriteDetails.toFavorite(): Favorite = Favorite(
    id = id,
    departureCode = departureCode,
    destinationCode = destinationCode
)