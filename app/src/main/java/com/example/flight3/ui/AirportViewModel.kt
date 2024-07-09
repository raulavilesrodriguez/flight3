package com.example.flight3.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flight3.data.Airport
import com.example.flight3.data.FlightRepository
import com.example.flight3.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AirportViewModel(
    private val flightRepository: FlightRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    var nameUiState by mutableStateOf(NameUiState())

    private fun validateInput(uiState: String = nameUiState.partName): Boolean{
        return uiState.isNotBlank()
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Update current airports ui state
     */
    var airportUiState : StateFlow<AirportsUiState> =
        if(validateInput(nameUiState.partName)){
            flightRepository.getAirportsStream("%${nameUiState.partName}%").map {
                AirportsUiState(airportList = it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = AirportsUiState()
                )
        } else {
            MutableStateFlow(AirportsUiState())
        }

    fun updateUiState(name: String){
        nameUiState = NameUiState(name)
        viewModelScope.launch {
            userPreferencesRepository.saveUserText(name)
        }
    }

    fun getUiState(): String{
        return nameUiState.partName
    }

    // UI states access to info of Data Store
    val userTextsUiState: StateFlow<TextsReleaseUIState> =
        userPreferencesRepository.userTexts.map { userTexts ->
            TextsReleaseUIState(userTexts, nameUiState)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TextsReleaseUIState()
            )


    val favoritesUiState: StateFlow<ListFavoritesUiState> =
        flightRepository.getFavoritesStream()
            .map {
                ListFavoritesUiState(it)
            } .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListFavoritesUiState()
            )

    suspend fun deleteFavorite(int: Int){
        flightRepository.deleteFavorite(int)
    }


}

/**
 * Ui State for name of Airport
 */
data class NameUiState(
    val partName: String = "",
)

/**
 * Ui State for AirportScreen
 */
data class AirportsUiState(
    val airportList: List<Airport> = listOf()
)

data class FavoritesUiState(
    val id: Int = 0,
    val nameDeparture: String = "",
    val nameDestination: String = "",
    val departureCode: String = "",
    val destinationCode: String = ""
)

data class ListFavoritesUiState(
    val favorites : List<FavoritesUiState> = listOf(),
)

/*
 * Data class containing various text enter by user
 */
data class TextsReleaseUIState(
    val userTexts: Set<String> = emptySet(),
    val name: NameUiState = NameUiState(),
    val filteredText: List<String> = userTexts.filter {
        it.contains(name.partName, ignoreCase = true)
    }
)