package com.example.flight3.data

import com.example.flight3.ui.FavoritesUiState
import com.example.flight3.ui.RoutesUiState
import kotlinx.coroutines.flow.Flow

interface FlightRepository {

    fun getAirportsStream(partName: String): Flow<List<Airport>>

    fun getAirportStream(id: Int): Flow<Airport?>

    fun getFlightsStream(id: Int): Flow<List<RoutesUiState>>

    suspend fun insertFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)

    fun getFavoritesStream(): Flow<List<FavoritesUiState>>
}